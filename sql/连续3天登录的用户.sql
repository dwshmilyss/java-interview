-- 连续3天登录的用户
-- 方法一
select user_id, count(1) cnt
from (select user_id, login_date, row_number() over (partition by user_id order by login_date) rn
      from last_n_days) as tmp
group by user_id, date_sub(login_date, interval tmp.rn day)
having count(1) >= 3;

-- 方法二
-- lead(login_date, 2, -1)表示login_date往下偏移2行，如果还是同一个user_id的，那就返回对应的login_date，否则返回-1
-- n天的话只需要把lead中的第二个参数改为(n-1)即可
select user_id
from (select user_id, login_date, lead(login_date, 2, -1) over (partition by user_id order by login_date) as date1
      from last_n_days) tmp
where date_add(tmp.login_date, interval 2 day) = date1;

-- 方法三
-- 第一步：确保数据按天去重（防止同一天多次登录干扰窗口函数）
WITH daily_logins AS (
    SELECT DISTINCT
        user_id,
        CAST(login_date AS DATE) AS login_dt
    FROM last_n_days
),

-- 第二步：使用 LEAD 函数获取该用户“下下次”登录的日期
     login_leads AS (
         SELECT
             user_id,
             login_dt,
             -- 获取按时间排序后的下第2条记录
             LEAD(login_dt, 2) OVER (PARTITION BY user_id ORDER BY login_dt) AS third_login_dt
         FROM daily_logins
     )


-- 第三步：计算日期差，如果差值为 2，则代表该行对应的日期开始是连续三天登录
SELECT
    user_id,
    login_dt AS start_date, -- 连登的第一天
    third_login_dt AS end_date -- 连登的第三天
FROM login_leads
WHERE third_login_dt IS NOT NULL
  AND DATEDIFF(third_login_dt, login_dt) = 2;

-- 【进阶版】如果是计算“三日留存率”
-- 通常定义：某日活跃用户在往后第3天再次活跃的比例
-- SQL 逻辑略有不同：
SELECT
    first_day.login_date,
    COUNT(DISTINCT first_day.user_id) as active_users, -- 当天的活跃用户数
    COUNT(DISTINCT future_day.user_id) as retained_users, -- 3天后的活跃用户数
    COUNT(DISTINCT future_day.user_id) * 1.0 / COUNT(DISTINCT first_day.user_id) as three_day_retention -- 3日留存率
FROM last_n_days first_day
LEFT JOIN last_n_days future_day
  ON first_day.user_id = future_day.user_id
  AND DATEDIFF(future_day.login_date, first_day.login_date) = 2
GROUP BY 1;