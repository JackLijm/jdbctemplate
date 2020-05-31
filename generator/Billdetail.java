package generator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * BillDetail
 * @author 
 */
@Data
public class Billdetail implements Serializable {
    /**
     * 交易账号
     */
    private String accountNo;

    /**
     * 交易流水
     */
    private String tradeNo;

    /**
     * 交易时间
     */
    private Date tradeTime;

    /**
     * 交易对方
     */
    private String tradeTo;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 交易金额
     */
    private BigDecimal tradeSum;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 交易状态
     */
    private String tradeStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 导入批次号
     */
    private String importBatch;

    private static final long serialVersionUID = 1L;
}