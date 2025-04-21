import request from '../utils/request'; // 你封装的 axios 实例

export function sendMessageApi(query, model) {
    return request.get('/helloworld/chat', {
        params: {
            query: query,
            model: model,
        },
    });
}

export function saveToRedis(sessionId, message) {
    return request.post('/helloworld/chat/save', {
        sessionId,
        text: message.text,
        isUser: message.isUser
    });
}
export function searchSimilarMessages(query, sessionId) {
    return request.get('/helloworld/chat/search', {
        params: { query, sessionId }
    });
}
