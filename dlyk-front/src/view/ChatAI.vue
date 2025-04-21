<template>
  <div class="chat-wrapper">
    <!-- 顶部导航 -->
    <header class="chat-header">
      <h1>Chat AI</h1>
      <el-select v-model="selectedModel" placeholder="选择模型" size="small" class="model-select">
        <el-option label="DashScope" value="dashscope" />
        <el-option label="OpenAI" value="openai" />
      </el-select>
    </header>

    <!-- 聊天区域 -->
    <div class="chat-box" ref="chatBoxRef">
      <div v-for="(message, index) in messages" :key="index" class="message">
        <div :class="message.isUser ? 'user-message' : 'bot-message'">
          <div class="bubble">
            <span class="role">{{ message.isUser ? '我：' : 'AI：' }}</span>
            {{ message.text }}
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="input-area">
      <el-input
          v-model="userInput"
          placeholder="请输入内容..."
          class="input"
          @keyup.enter="sendMessage"
          clearable
      />
      <el-button type="primary" @click="sendMessage">发送</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { sendMessageApi } from '../api/chat.js'

const userInput = ref('')
const selectedModel = ref('dashscope')
const messages = ref([{ text: '你好，我是智能助手，有什么可以帮你？', isUser: false }])
const chatBoxRef = ref(null)

const sendMessage = async () => {
  if (!userInput.value.trim()) return
  const input = userInput.value
  messages.value.push({ text: input, isUser: true })
  userInput.value = ''

  try {
    const response = await sendMessageApi(input, selectedModel.value)
    const botReply = response.data || '暂无回复'
    messages.value.push({ text: botReply, isUser: false })
  } catch (err) {
    messages.value.push({ text: '出错了：' + err.message, isUser: false })
  }

  await nextTick(() => {
    const el = chatBoxRef.value
    el.scrollTop = el.scrollHeight
  })
}
</script>

<style scoped>
.chat-wrapper {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f7fa;
  font-family: 'Segoe UI', sans-serif;
}

/* 顶部导航栏 */
.chat-header {
  height: 60px;
  background-color: #ffffff;
  border-bottom: 1px solid #e0e0e0;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-header h1 {
  font-size: 18px;
  font-weight: bold;
  margin: 0;
  color: #333;
}

.model-select {
  width: 160px;
}

/* 聊天区域 */
.chat-box {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f0f2f5;
}

.message {
  display: flex;
  margin-bottom: 16px;
  width: 100%;
}

.user-message {
  justify-content: flex-end;
}

.bot-message {
  justify-content: flex-start;
}
.bubble {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 16px;
  font-size: 14px;
  line-height: 1.6;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  word-wrap: break-word;
  white-space: pre-wrap;
}

/* 用户消息靠右 + 蓝色背景 */
.user-message .bubble {
  background-color: #d6eaff;
  color: #000;
  border-bottom-right-radius: 0;
  text-align: left;
}

/* AI消息靠左 + 灰色背景 */
.bot-message .bubble {
  background-color: #f0f0f0;
  color: #333;
  border-bottom-left-radius: 0;
  text-align: left;
}

.role {
  font-size: 12px;
  color: #888;
  display: block;
  margin-bottom: 4px;
}

/* 输入区域 */
.input-area {
  display: flex;
  padding: 16px;
  border-top: 1px solid #eaeaea;
  background: #fff;
  gap: 12px;
}

.input-area .input {
  flex: 1;
}
</style>
