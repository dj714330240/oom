package com.coe.oom.core.base.util.vali.excel;



import com.coe.oom.core.base.entity.Message;
import com.coe.oom.core.base.util.NumberUtil;
import com.coe.oom.core.base.util.StringUtil;

import java.util.List;

/**
 * 校验
 * @author lqg
 *
 */
public class ValiUtil {

	/** 
	 * 校验集合某列不能为空
	 * 
	 * @param valiArrList
	 * @param indexMessage
	 * @return
	 */
	public static Message valiList(List<String> valiArrList, List<ValiHelp> listVali) {
		StringBuffer result = new StringBuffer();
		for (ValiHelp valiHelp : listVali) {
			Integer index = valiHelp.getIndex();
			String msg = valiHelp.getMsg();
			boolean isAllowNull = valiHelp.getIsAllowNull();
			ValiHelpEnum type = valiHelp.getType();
			String regular = valiHelp.getRegular();

			if ((index.intValue() + 1) > valiArrList.size()) {
				result.append(msg + ";");
				continue;
			}
			Object value = valiArrList.get(index);
			// 如果不允许为空 但为空
			if ((!isAllowNull) && StringUtil.isNull(value.toString())) {
				result.append(msg + ";");
				continue;
			}
			// 如果不为空
			if (!StringUtil.isNull(value.toString())) {
				// 如果是number类型
				if (StringUtil.isEqual(type.getTypeCode(), ValiHelpEnum.NUMBER_TYPE.getTypeCode())) {
					if (!NumberUtil.isNumberic(value.toString())) {
						result.append(msg + ";");
						continue;
					}
					
					//最小长度
					Integer minLength = valiHelp.getMinLength();
					//最大长度
					Integer maxLength = valiHelp.getMaxLength();
					
					if(minLength!=-1 && regular.length()<minLength){
						result.append(msg + ";");
						continue;
					}

					if(maxLength!=-1 && regular.length()>minLength){
						result.append(msg + ";");
						continue;
					}
				}else if(StringUtil.isEqual(type.getTypeCode(), ValiHelpEnum.BIGDECIMAL_TYPE.getTypeCode())){
					if (!NumberUtil.isBigDecimal(value.toString())) {
						result.append(msg + ";");
						continue;
					}
				}
				
				if (!StringUtil.isNull(regular)) {
					if (!value.toString().matches(regular)) {
						result.append(msg + ";");
						continue;
					}
				}

			}

		}
		String resultStr = result.toString();
		if(StringUtil.isNull(resultStr)){
			return Message.success("校验通过");
		}
		return Message.error(resultStr);
	}
}
