var List = Java.type('java.util.ArrayList');
var ApplicationContextUtil = Java.type("com.blog.base.util.ApplicationContextUtil");
var Map = Java.type("java.util.HashMap");
// import

function ex(args) {
    // var System = Java.type("java.lang.System");
    // System.out.println(args);
    // System.out.println(args.orderId);
    //定义Java对象

    // var obj = new Obj();

    //调用Java对象的方法
    var blogService = ApplicationContextUtil.get("blogServiceImpl");
    // //args.xxx获取Map中的参数
    // var orderInfo = orderInfoService.get(args.orderId);
    // if (orderInfo == null) {
    //     return obj;
    // }
    // var map = new java.util.HashMap();
    // map.put("orderCode", orderInfo.getCode());
    // map.put("clientCode", orderInfo.getClientCodes());
    // obj.add(map);
    // var blogs = blogService.getBlog();
    var list = new List();
    var blog = blogService.getBlog(args.blogId);
    var blogs = blogService.getIndexBlog();
    list.add(blogs);
    list.add(blog);
    // map.put("1",blog);
    return list;

}
