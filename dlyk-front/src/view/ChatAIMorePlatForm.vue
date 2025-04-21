<template>
  <div class="chatai-layout">
    <!-- 左侧会话列表 -->
    <div class="chat-history">
      <div class="chat-history-header">
        <h2>对话列表</h2>
        <el-button type="success" @click="createNewSession" size="small">+ 新会话</el-button>
      </div>
      <ul class="session-list">
        <li
            v-for="(session, index) in sessions"
            :key="index"
            :class="{ active: currentSessionIndex === index }"
            @click="switchSession(index)"
        >
          会话 {{ index + 1 }}
        </li>
      </ul>
    </div>

    <!-- 右侧主聊天窗口 -->
    <div class="chatai-container">
      <header class="chat-header">
        <h1>Chat AI</h1>
      </header>

      <div class="chat-box" ref="chatBoxRef">
        <div
            v-for="(message, index) in currentMessages"
            :key="index"
            :class="['message-card', message.isUser ? 'user-message' : 'bot-message']"
        >
          <div class="bubble-card">
            <div class="bubble-text">{{ message.text }}</div>
          </div>
        </div>
      </div>

      <!-- 固定底部输入栏 -->
      <div class="input-bar-fixed">
        <div class="model-and-input">
          <el-select v-model="selectedModel" size="small" class="model-dropdown">
            <el-option label="DashScope" value="dashscope" />
            <el-option label="OpenAI" value="openai" />
          </el-select>
          <el-input
              v-model="userInput"
              placeholder="输入消息..."
              @keyup.enter="sendMessage"
              clearable
              class="chat-input"
          />
          <el-button type="primary" @click="sendMessage">发送</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue';
import { saveToRedis, sendMessageApi, searchSimilarMessages } from '../api/chatAIMorePlatForm.js';

const userInput = ref('');
const selectedModel = ref('dashscope');
const chatBoxRef = ref(null);
const sessions = ref([
  [{ text: '你好，我是智能助手，有什么可以帮你？', isUser: false }],
]);
const currentSessionIndex = ref(0);
const currentSessionId = computed(() => `session-${currentSessionIndex.value}`);
const currentMessages = computed(() => sessions.value[currentSessionIndex.value]);

const sendMessage = async () => {
  if (!userInput.value.trim()) return;
  const sessionId = currentSessionId.value;
  const input = userInput.value;
  currentMessages.value.push({ text: input, isUser: true });
  userInput.value = '';

  await nextTick(() => {
    chatBoxRef.value.scrollTop = chatBoxRef.value.scrollHeight;
  });

  try {
    await saveToRedis(sessionId, { text: input, isUser: true });
    const searchRes = await searchSimilarMessages(input, sessionId);
    const similarList = searchRes?.data?.data || [];
    const contextText = similarList.join('\n') + '\n' + input;
    const response = await sendMessageApi(contextText, selectedModel.value);
    const botText = response?.data?.data || '暂无响应';
    currentMessages.value.push({ text: botText, isUser: false });
    await saveToRedis(sessionId, { text: botText, isUser: false });
  } catch (err) {
    currentMessages.value.push({ text: '出错了：' + err.message, isUser: false });
  }

  await nextTick(() => {
    chatBoxRef.value.scrollTop = chatBoxRef.value.scrollHeight;
  });
};

const switchSession = (index) => {
  currentSessionIndex.value = index;
};

const createNewSession = () => {
  sessions.value.push([{ text: '新会话开始啦，问我点什么吧！', isUser: false }]);
  currentSessionIndex.value = sessions.value.length - 1;
};
</script>

<style scoped>
.chatai-layout {
  display: flex;
  height: 100vh;
  background: #f2f4f8;
  font-family: 'Segoe UI', sans-serif;
}

.chat-history {
  width: 240px;
  background: #fff;
  border-right: 1px solid #e5e5e5;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.03);
  padding: 16px;
  box-sizing: border-box;
  z-index: 2;
}

.chat-history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.session-list li {
  padding: 8px;
  cursor: pointer;
  border-radius: 6px;
  transition: background 0.2s;
}

.session-list li.active {
  background-color: #e1eaff;
  font-weight: bold;
}

.session-list li:hover {
  background-color: #f0f4ff;
}

.chatai-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 0;
}

.chat-header {
  height: 60px;
  background: #fff;
  border-bottom: 1px solid #eaeaea;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.chat-box {
  flex: 1;
  padding: 30px 40px;
  overflow-y: auto;
  background-color: #f7f9fb;
  border-left: 1px solid #f0f0f0;
}

.message-card {
  display: flex;
  margin-bottom: 20px;
}

.user-message {
  justify-content: flex-end;
}

.bot-message {
  justify-content: flex-start;
}

.bubble-card {
  max-width: 70%;
  padding: 14px 18px;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
  transition: all 0.3s;
}

.user-message .bubble-card {
  background-color: #d6eaff;
  border-bottom-right-radius: 0;
}

.bot-message .bubble-card {
  background-color: #f0f0f0;
  border-bottom-left-radius: 0;
}

.input-bar-fixed {
  position: sticky;
  bottom: 0;
  background: #ffffff;
  padding: 14px 20px;
  border-top: 1px solid #e0e0e0;
  box-shadow: 0 -3px 8px rgba(0, 0, 0, 0.04);
  z-index: 10;
}

.model-and-input {
  display: flex;
  gap: 12px;
  align-items: center;
}

.model-dropdown {
  width: 160px;
}

.chat-input {
  flex: 1;
}
</style>
