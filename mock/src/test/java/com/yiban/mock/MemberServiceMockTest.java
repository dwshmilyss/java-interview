package com.yiban.mock;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

/**
 * @author david.duan
 * @packageName com.yiban.java.junit.mock
 * @className MemberServiceMockTest
 * @date 2025/10/23
 * @description
 *
 * @Resource 真实调用
 * @MockBean 没有指定规则走默认，有规则就走规则
 *
 */
@SpringBootTest
public class MemberServiceMockTest {

    //注意 同一个类型的bean上，MockBean和SpyBean冲突，所以这个类中针对MemberService不能既有@MockBean又有@SpyBean
//    @Resource //真实调用
//    @MockBean // mock 造假
    @SpyBean // 第三组 spy 半真半假
    private MemberService memberService;

    /*
    第一组 测试真实调用
     */
    @Test
    void m1() {
        String res = memberService.add(2);
        assertEquals("ok", res);
    }

    /*
    第二组 mock 造假
     */
    // 如果不指定规则，mock默认会返回固定的值(如果原方法的返回值类型是引用类型就返回null，如果是基本类型就返回默认值，比如int=0)
    @Test
    void m2() {
        String res = memberService.add(2);
        assertEquals("ok", res);//这里断言就会失败，因为
    }

    /*
     * MockBean 如果指定了规则，则按规则返回值
     * 比如这里当我用mock调用MemberService.add(2)的时候，我就让mock给我伪造个返回值是"ok",这样我就能跑接下里的流程
     * 这在微服务中测试方法时很常用，因为往往微服务中会调用其他的微服务，当调用链比较长的时候，而我又只想测试我自己的方法，
     * 那么我在调别人服务的时候就可以让mock帮我伪造一份正常的数据(尤其是在别人的服务涉及到写数据库或中间件的时候就非常有用)
      */
    @Test
    void m3() {
        when(memberService.add(2)).thenReturn("ok");//指定规则
        String res = memberService.add(2);
        assertEquals("ok", res);
    }


    /*
    第三组  半真半假
    add()造假
    del()走真实调用
     */
    @Test
    void m4() {
        //这两种写法均可，但是AI推荐doReturn的写法
//        when(memberService.add(2)).thenReturn("ok");
        doReturn("ok").when(memberService).add(2);
        String res = memberService.add(2);
        System.out.println("------ add res = " + res);
        assertEquals("ok", res);

//        doReturn(3).when(memberService).del(3);
        int res2 = memberService.del(3);
        System.out.println("------ del res2 = " + res2);
        assertEquals(3, res2);
    }
}
