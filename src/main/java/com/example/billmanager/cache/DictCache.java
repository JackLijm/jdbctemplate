/**
 * <p>文件名称: DictCache.java</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2016-</p>
 * <p>公    司: 金证财富南京科技有限公司</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>创建日期： 2021/6/11 11:15 </p>
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
package com.example.billmanager.cache;

import com.example.billmanager.dto.SysDictKeyDTO;
import org.springframework.stereotype.Component;

@Component
public class DictCache extends BaseRedisHashCache<SysDictKeyDTO> {

    private static String preKey = "sys:";

    private static String afterKey = ":first";


    /**
     * 根据业务key获得redis中实际存储的key
     *
     * @param key 业务key
     * @return redis中存储的真实key
     */
    @Override
    protected String getKey(String key) {
        return preKey + key + afterKey;
    }

}
