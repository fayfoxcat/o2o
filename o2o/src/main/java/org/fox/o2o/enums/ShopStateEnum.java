package org.fox.o2o.enums;

public enum ShopStateEnum {
	CHECK(0, "审核中"), OFFLINE(-1, "非法店铺"), SUCCESS(1, "操作成功"), PASS(2, "认证通过"), INNER_ERROR(-1001, "内部系统错误"),
	NULL_SHOPID(-1002, "ShopId为空"), NULL_SHOP_INFO(-1003, "传入了空的信息");
	private int state;
	private String stateInfo;
	
	//构造函数，将上述enum值分别传入state及stateInfo
	private ShopStateEnum(int state,String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	/**
	 * 根据传入的 state返回相应的enum值
	 * @param state
	 * @return
	 */
	public static ShopStateEnum stateOf(int state){
		for(ShopStateEnum stateEnum:values()) {
			if(stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}
	
	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
}
