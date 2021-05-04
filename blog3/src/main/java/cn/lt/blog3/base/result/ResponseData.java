package cn.lt.blog3.base.result;

import cn.lt.blog3.base.em.ResponseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.util.List;
import java.util.Map;

/**
 * 返回数据
 *
 * @author lt
 * @date 2021/4/7 18:12
 */
@Data
@ApiModel("返回数据")
public class ResponseData<T>  {

    @ApiModelProperty("状态码，默认成功。")
    private ResponseStatus status;

    private int code = 200;

    @ApiModelProperty("响应某些提示")
    private String msg;

    @ApiModelProperty("数据")
    private T data;

    public ResponseData() {
    }

    public ResponseData(ResponseStatus status) {
        this.status = status;
    }

    public ResponseData(int code) {
        this.code = code;
    }

    public ResponseData(ResponseStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResponseData(ResponseStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    public ResponseData(T data) {
        this.data = data;
    }

    /**
     * 默认
     * @return
     */
    public static ResponseData data(){
        ResponseData data = new ResponseData();
        return data;
    }

    /**
     *  状态
     * @param status
     * @return
     */
    public static ResponseData data(ResponseStatus status){
        ResponseData data = new ResponseData();
        return data;
    }

    /**
     * boolean对象
     * @param <T>
     * @param t
     * @return
     */
    public static <T> ResponseData<T> data(T t) {
        ResponseData responseData = new ResponseData(t);
        if (t instanceof Boolean) {
            responseData.setStatus((Boolean) t ? ResponseStatus.SUCCESS : ResponseStatus.FAIL);
        }
        return responseData;
    }

    /**
     * 集合
     * @param t
     * @param <T>
     * @return
     */
    public static <T>ResponseData<T> data(List<T> t){
        ResponseData data = new ResponseData(t);
        System.out.println(data.data);

        return data;
    }

    /**
     * Map
     * @param param
     * @return
     */
    public static ResponseData data(Map<String, Object> param) {
        ResponseData responseData = new ResponseData(param);
        return responseData;
    }

    /**。
     * 状态和错误信息
     * @param status
     * @param message
     * @return
     */
    public static ResponseData data(ResponseStatus status, String message) {
        ResponseData responseData = new ResponseData(status,"");
        return responseData;
    }


//    @Override
//    public ResponseData put(String key, Object value) {
//        super.put(key, value);
//        return this;
//    }
}
