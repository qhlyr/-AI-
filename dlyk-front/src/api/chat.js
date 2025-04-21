// src/api/chat.js
import request from '../utils/request.js';

// 发送用户输入到后端接口
export const sendMessageApi = (query) => {
    return request.get('/helloworld/simple/chat', { params: { query } })
        .then(response => {
            console.log('API Response:', response); // 打印整个响应对象
            return response.data; // 返回响应对象
        })
        .catch(error => {
            // 检查 error 是否存在 response 或 request 属性
            if (error && error.response) {
                console.error('API Error Response:', error.response);
            } else if (error && error.request) {
                console.error('API Error Request:', error.request);
            } else if (error && error.message) {
                console.error('API Error Message:', error.message);
            } else {
                console.error('API Unknown Error:', error);
            }
            throw new Error('API 请求失败');
        });
};
