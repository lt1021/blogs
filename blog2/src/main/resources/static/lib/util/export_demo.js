function ex(args) {
    // var System = Java.type("java.lang.System");
    // System.out.println(args);
    // System.out.println(args.orderId);
    //定义Java对象
    var Obj = Java.type('java.util.ArrayList');
    var obj = new Obj();
    // var ApplicationContextUtil = Java.type("com.we7.erp.base.util.ApplicationContextUtil");
    var ApplicationContextUtil = Java.type("com.blog.base.util.ApplicationContextUtil");
    //调用Java对象的方法
    var orderInfoService = ApplicationContextUtil.getBean("orderInfoServiceImpl");
    //args.xxx获取Map中的参数
    var orderInfo = orderInfoService.get(args.orderId);
    if (orderInfo == null) {
        return obj;
    }
    var map = new java.util.HashMap();
    map.put("orderCode", orderInfo.getCode());
    map.put("clientCode", orderInfo.getClientCodes());
    obj.add(map);
    return obj;

}
