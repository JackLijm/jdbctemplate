/**
 * <p>文件名称: EntryFactory.java</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2016-</p>
 * <p>公    司: 金证财富南京科技有限公司</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>创建日期： 2021/2/6 13:13 </p>
 * <p>完成日期：</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 * <p>修改记录2：…</p>
 *
 * @version 1.0
 * @author lijm@szkingdom.com
 */
package com.example.billmanager.brickyard.entry;

import java.util.HashMap;
import java.util.Map;

public class EntryFactory {
    private Map<String, Object> entryMap = new HashMap<>();

    public <T> T getEntry(Class<T> nClass) {
        String name = nClass.getName();
        Object o = entryMap.get(name);
        if (o == null) {
            try {
                T t = nClass.newInstance();
                entryMap.put(name, t);
                o = t;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            return (T) o;
        }
        return (T) o;
    }
}
