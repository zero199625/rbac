

import request from "@/utils/request";


let moduleName = "/topic"

export default {


    /**
     *  分页查询帖子
     * @param data
     * @returns {Promise<AxiosResponse<*>>|*}
     */
    getByPage(data){
        return request({
            url: `${moduleName}/getByPage`,
            method: "post",
            data
        })
    },


    /**
     *  发表帖子
     * @param data
     * @returns {Promise<AxiosResponse<*>>|*}
     */
    save(data){
        return request({
            url: `${moduleName}/save`,
            method: "post",
            data
        })
    },


    /**
     *  根据ID获取帖子信息
     * @param topicId
     * @returns {Promise<AxiosResponse<*>>|*}
     */
    getTopicById(topicId){
        return request({
            url: `${moduleName}/getTopicById/`+topicId,
            method: "get"
        })
    },


    update(topicId){
        return request({
            url: `${moduleName}/update/`+topicId,
            method: "put"
        })
    }
}






















