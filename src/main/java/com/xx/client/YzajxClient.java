package com.xx.client;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class YzajxClient {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取JX信息
     */
    public String getAjxInfo() {
        String apiUrl = "https://www.yzajx.cn/api/info";

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sub-J", "123456");
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("接口调用响应内容（POST请求）:{} ", responseEntity.getBody());
                return responseEntity.getBody();
                // 同样可根据业务需求进一步解析处理responseBody
            } else {
                log.error("接口调用失败，状态码: {}", responseEntity);
            }
        } catch (Exception e) {
            log.error("https://www.yzajx.cn/api/info ERROR:", e);
        }
        return null;
    }


    /**
     * 发布文章
     */
    public String postArticles() {
        String apiUrl = "https://admin.yzajx.cn/api/admin/articles";

        String params = "{\"id\":143,\"articleTitle\":\"手写可直接套用的左右轮播组件\",\"articleAbstract\":\"\",\"articleContent\":\"```vue\\n<template>\\n    <div class=\\\"card-box\\\">\\n\\t    <!-- 左边按钮-->\\n        <div class=\\\"btn left-btn\\\" @click=\\\"switchFun(-1)\\\" :class=\\\"{'show':leftBtnShow }\\\"></div>\\n\\t\\t<!-- 中间区域，宽度为自定义的可视区域宽度即可-->\\n        <div class=\\\"card-container\\\" id=\\\"J_cardContainer\\\">\\n\\t\\t    <!-- 卡片的容器，因为width不固定，所以根据子项多少计算出来，保障是在一行里-->\\n            <div class=\\\"card-ul\\\"\\n                 :style=\\\"{\\n                     width: `${cardArr.length ? cardWidth * cardArr.length : 'auto'}px`,\\n                     transform: `translateX(${translateX}px)`\\n                 }\\\"\\n            >\\n\\t\\t\\t\\t<!-- 每个卡片里的内容，可随意自定义-->\\n                <div class=\\\"card\\\" v-for=\\\"(item,index) in cardArr\\\" :key=\\\"index\\\">\\n                    <span :title=\\\"item.name\\\">{{item.name}}</span>\\n                </div>\\n            </div>\\n        </div>\\n\\t    <!-- 右边按钮-->\\n        <div class=\\\"btn right-btn\\\" @click=\\\"switchFun(1)\\\" :class=\\\"{'show':rightBtnShow}\\\"></div>\\n    </div>\\n</template>\\n<script>\\n    export default {\\n        name: 'middleView',\\n        props: {\\n            //卡片的宽度+左右边距\\n            cardWidth: {\\n                type: Number,\\n                default: 260\\n            }\\n        },\\n        data() {\\n            return {\\n                //轮播的位移值\\n                translateX: 0,\\n                //轮播的移动步数\\n                moved: 0,\\n                //容器能能展示最多的卡片数量\\n                maxShowLen: 7,\\n                //轮播的数据\\n                cardArr: [\\n                    {name: \\\"非机动车登记数据\\\"},\\n                    {name: \\\"就医数据\\\"},\\n                    {name: \\\"银行卡数据\\\"},\\n                    {name: \\\"数据统计4\\\"},\\n                    {name: \\\"数据统计5\\\"},\\n                    {name: \\\"数据统计6\\\"},\\n                    {name: \\\"数据统计7\\\"},\\n                    {name: \\\"数据统计8\\\"},\\n                    {name: \\\"数据统计9\\\"},\\n                ]\\n            };\\n        },\\n        mounted() {\\n            // 最大长度，maxShowLen建议可以写死，因为卡片最后一个的边距会影响这个值,\\n\\t\\t\\t//或者maxShowLen通过以下计算可得，宽度加上卡片的一个右边距，比如13px\\n            // this.maxShowLen = Math.floor((document.querySelector(\\\"#J_cardContainer\\\").clientWidth+13) / this.cardWidth);\\n        },\\n        computed: {\\n            //左边按钮显隐，true显示\\n            leftBtnShow() {\\n                return this.moved !== 0\\n            },\\n            //右边按钮显隐，true显示\\n            rightBtnShow() {\\n                //或者实际卡片长度>移动的数量+能装最大长度\\n                return this.cardArr.length > Math.abs(this.moved) + this.maxShowLen\\n            },\\n        },\\n        methods: {\\n            /**\\n             * 轮播左右按钮\\n             * @method clickPrev\\n             * @param {Number}flag   -1 左按钮   1右按钮\\n             * @returns{void}\\n             */\\n            switchFun(flag) {\\n                if (flag === -1 && this.leftBtnShow || flag === 1 && this.rightBtnShow) {\\n                    this.moved += flag * 1;\\n                    this.translateX = -(this.moved * this.cardWidth);\\n                }\\n            },\\n        },\\n    };\\n</script>\\n<style lang=\\\"less\\\" scoped>\\n    .card-box {\\n        position: relative;\\n        display: flex;\\n        align-items: center;\\n        width: 100%;\\n        height: 90px;\\n\\n        .btn {\\n            position: relative;\\n            width: 37px;\\n            height: 57px;\\n            background-repeat: no-repeat;\\n            background-position: center center;\\n            background-size: 100% 100%;\\n            cursor: default;\\n\\n            &.left-btn {\\n                position: absolute;\\n                left: 10px;\\n                background-image: url(\\\"../img/adv-left-btn.png\\\");\\n\\n                &.show {\\n                    background-image: url(\\\"../img/adv-left-btn-active.png\\\");\\n                    cursor: pointer;\\n                }\\n            }\\n\\n            &.right-btn {\\n                position: absolute;\\n                right: 10px;\\n                background-image: url(\\\"../img/adv-right-btn.png\\\");\\n\\n                &.show {\\n                    background-image: url(\\\"../img/adv-right-btn-active.png\\\");\\n                    cursor: pointer;\\n                }\\n            }\\n        }\\n\\n        .card-container {\\n            position: absolute;\\n            top: 50%;\\n            left: 50%;\\n            transform: translate(-50%, -50%);\\n            width: 94%;\\n            height: 100%;\\n            overflow: hidden;\\n\\n            .card-ul {\\n                height: 100%;\\n                display: flex;\\n                transition: transform .3s linear;\\n\\n                .card {\\n                    display: flex;\\n                    align-items: center;\\n                    width: 247px;\\n                    height: 100%;\\n                    padding: 19px 17px 19px 88px;\\n                    background-repeat: no-repeat;\\n                    background-position: center center;\\n                    background-size: 100% 100%;\\n                    margin-right: 13px;\\n                    background-image: url(\\\"../img/nonvehicle-icon.png\\\");\\n                    cursor: pointer;\\n\\n                    > span {\\n                        display: inline-block;\\n                        width: 100%;\\n                        font-family: AlibabaPuHuiTi-Heavy;\\n                        font-size: 16px;\\n                        color: #b4c8e3;\\n                        overflow: hidden;\\n                        white-space: nowrap;\\n                        text-overflow: ellipsis;\\n                    }\\n                }\\n            }\\n\\n        }\\n    }\\n</style>\\n```\",\"articleCover\":\"https://image.yzajx.cn/aurora/articles/ebcc30f9c748fd2f729392f494409b3f.jpg\",\"categoryName\":\"前端\",\"tagNames\":[\"VUE\",\"工具\",\"组件\"],\"isTop\":1,\"isFeatured\":1,\"status\":1,\"type\":1,\"originalUrl\":null,\"password\":null}\n";
        JSONObject json = JSONObject.parseObject(params);
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmMjA1NzgyZWM3MzY0NTllYmI5NDRkMWRjOWFjZjBmMSIsInN1YiI6IjEiLCJpc3MiOiJodWF3ZWltaWFuIn0.PQxlbEZJH70MabCUfAIFgDzI5pbzFswGPTMSrXcF6oc");
        HttpEntity<Object> requestEntity = new HttpEntity<>(json, headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("接口调用响应内容（POST请求）:{} ", responseEntity.getBody());
                return responseEntity.getBody();
                // 同样可根据业务需求进一步解析处理responseBody
            } else {
                log.error("接口调用失败，状态码: {}", responseEntity);
            }
        } catch (Exception e) {
            log.error("https://admin.yzajx.cn/api/admin/articles ERROR:", e);
        }
        return null;
    }
}
