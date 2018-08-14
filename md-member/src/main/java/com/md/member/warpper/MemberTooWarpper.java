package com.md.member.warpper;

import java.util.List;
import java.util.Map;

import com.md.member.factory.MemberFactory;
import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.constant.Gender;
import com.stylefeng.guns.core.constant.Status;

/**
 * 客户的包装类
 *
 * @authr 
 * @date 2017年2月19日15:07:29
 */
public class MemberTooWarpper extends BaseControllerWarpper {

    public MemberTooWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
    	map.put("name", MemberFactory.me().getMemberName(Long.valueOf(String.valueOf(map.get("memberId")))));
    	map.put("phone", MemberFactory.me().getPhoneNum(Long.valueOf(String.valueOf(map.get("memberId")))));
    }

}
