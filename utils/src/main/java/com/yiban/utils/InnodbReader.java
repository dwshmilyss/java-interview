// java
package com.yiban.utils;

import com.alibaba.innodb.java.reader.TableReader;
import com.alibaba.innodb.java.reader.TableReaderImpl;
import com.alibaba.innodb.java.reader.page.index.GenericRecord;
import com.alibaba.innodb.java.reader.schema.Column;
import com.alibaba.innodb.java.reader.schema.TableDef;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class InnodbReader {
    /**
     * 隐藏字段信息类
     */
    public static class HiddenFields {
        private Long dbTrxId;      // 6字节事务ID
        private Long dbRollPtr;    // 7字节回滚指针
        private Long dbRowId;      // 6字节行ID（无主键时）

        // 构造函数、getter和setter省略

        @Override
        public String toString() {
            return String.format(
                    "HiddenFields{DB_TRX_ID=%d, DB_ROLL_PTR=0x%x, DB_ROW_ID=%s}",
                    dbTrxId != null ? dbTrxId : 0,
                    dbRollPtr != null ? dbRollPtr : 0,
                    dbRowId != null ? dbRowId.toString() : "N/A"
            );
        }
    }

    public static void main(String[] args) throws Exception {
        detailedHiddenFieldsReader(createTableDef());
    }

    public static void detailedHiddenFieldsReader(TableDef tableDef) {

        String ibdFilePath = "/usr/local/mysql/data/test/user.ibd";

        try (TableReader reader = new TableReaderImpl(ibdFilePath,tableDef)) {
            reader.open();
            // 读取所有记录
            Iterator<GenericRecord> iterator = reader.getQueryAllIterator();
            int count = 0;

            while (iterator.hasNext()) {
                GenericRecord record = iterator.next();
                count++;

                System.out.println("=== 记录 #" + count + " ===");

                Object[] values = record.getValues();
                System.out.println(Arrays.asList(values));
                // 读取用户字段
//                System.out.println("用户字段:");
//                System.out.println("  id: " + record.get("id"));
//                System.out.println("  name: " + record.get("name"));
//
//                // 读取隐藏字段
//                HiddenFields hiddenFields = extractHiddenFields(record);
//                System.out.println("\n隐藏字段:");
//                System.out.println("  " + hiddenFields);
//                System.out.println();
            }

            System.out.println("总记录数: " + count);

        } catch (
                Exception e) {
            System.err.println("读取表文件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String formatRollPtr(Object obj) {
        if (obj instanceof byte[]) {
            StringBuilder sb = new StringBuilder("0x");
            for (byte b : (byte[]) obj) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        return String.valueOf(obj);
    }

    /**
     * 从GenericRecord中提取隐藏字段
     */
    private static HiddenFields extractHiddenFields(GenericRecord record) {
        HiddenFields fields = new HiddenFields();

        try {
            // 尝试通过反射或直接访问获取隐藏字段
            // 注意：innodb-java-reader可能需要特定方法访问这些字段

            // 方法1: 直接获取（如果API支持）
            Object trxId = record.get("DB_TRX_ID");
            if (trxId instanceof Number) {
                fields.dbTrxId = ((Number) trxId).longValue();
            }

            Object rollPtr = record.get("DB_ROLL_PTR");
            if (rollPtr instanceof Number) {
                fields.dbRollPtr = ((Number) rollPtr).longValue();
            }

            Object rowId = record.get("DB_ROW_ID");
            if (rowId instanceof Number) {
                fields.dbRowId = ((Number) rowId).longValue();
            }

        } catch (Exception e) {
            System.err.println("提取隐藏字段时出错: " + e.getMessage());
        }

        return fields;
    }

    /**
     * 定义表结构（根据实际表结构修改）
     */
    private static TableDef createTableDef() {
        return new TableDef()
                .setName("user")
                // 添加你的表字段定义
                .addColumn(new Column().setName("id").setType("INT").setNullable(false).setPrimaryKey(true))
                // 显式定义隐藏字段，让工具去读取对应的 6 字节和 7 字节
                .addColumn(new Column().setName("name").setType("VARCHAR(32)").setNullable(true));
    }


    /**
     * 读取并打印隐藏字段
     */
    private static void readHiddenFields(TableReader reader) {
        System.out.println("开始读取InnoDB隐藏字段...\n");
        System.out.println(String.format("%-10s %-20s %-20s %-20s",
                "记录号", "DB_TRX_ID", "DB_ROLL_PTR", "DB_ROW_ID"));
        System.out.println("=".repeat(80));

        Iterator<GenericRecord> iterator = reader.getQueryAllIterator();
        int recordCount = 0;

        while (iterator.hasNext()) {
            GenericRecord record = iterator.next();
            recordCount++;

            // 获取三个隐藏字段
            Long dbTrxId = getDbTrxId(record);
            Long dbRollPtr = getDbRollPtr(record);
            Long dbRowId = getDbRowId(record);

            // 打印隐藏字段
            System.out.println(String.format("%-10d %-20s %-20s %-20s",
                    recordCount,
                    dbTrxId != null ? dbTrxId : "NULL",
                    dbRollPtr != null ? "0x" + Long.toHexString(dbRollPtr) : "NULL",
                    dbRowId != null ? dbRowId : "NULL"));

            // 打印用户字段（可选）
            printUserFields(record);
        }

        System.out.println("\n总记录数: " + recordCount);
    }

    /**
     * 获取DB_TRX_ID（6字节的事务ID）
     */
    private static Long getDbTrxId(GenericRecord record) {
        try {
            // DB_TRX_ID是隐藏字段，通常在记录头部
            Object value = record.get("DB_TRX_ID");
            if (value != null) {
                return ((Number) value).longValue();
            }
        } catch (Exception e) {
            // 字段可能不存在或无法访问
        }
        return null;
    }

    /**
     * 获取DB_ROLL_PTR（7字节的回滚指针）
     */
    private static Long getDbRollPtr(GenericRecord record) {
        try {
            Object value = record.get("DB_ROLL_PTR");
            if (value != null) {
                return ((Number) value).longValue();
            }
        } catch (Exception e) {
            // 字段可能不存在或无法访问
        }
        return null;
    }

    /**
     * 获取DB_ROW_ID（6字节的行ID，仅在表没有主键时存在）
     */
    private static Long getDbRowId(GenericRecord record) {
        try {
            Object value = record.get("DB_ROW_ID");
            if (value != null) {
                return ((Number) value).longValue();
            }
        } catch (Exception e) {
            // 如果表有主键，这个字段不存在
        }
        return null;
    }

    /**
     * 打印用户自定义字段
     */
    private static void printUserFields(GenericRecord record) {
        System.out.println("  用户字段:");
        List<Object> values = Arrays.stream(record.getValues()).toList();
        for (int i = 0; i < values.size(); i++) {
            System.out.println("    字段" + i + ": " + values.get(i));
        }
        System.out.println();
    }
}
