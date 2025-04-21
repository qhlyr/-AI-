<script setup>
import { ref } from 'vue'
import { loginApi } from '../api/user.js'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import useUserStore from '../store/index.js'

const loginRules = {
  loginAct: [
    { required: true, message: '请输入登录账号', trigger: 'blur' }
  ],
  loginPwd: [
    { required: true, message: '请输入登录密码', trigger: 'blur' }
  ]
}

const user = ref({
  loginAct: '',
  loginPwd: '',
  rememberMe: false
})
const router = useRouter()
const userStore = useUserStore()
const loginRefForm = ref(null)

const login = async () => {
  await loginRefForm.value.validate()
  const { data } = await loginApi(user.value)
  userStore.setUserInfo({
    ...data.data,
    token: data.msg,
    rememberMe: user.value.rememberMe
  })
  ElMessage.success('登录成功')
  await router.replace('/')
}
</script>

<template>
  <el-container class="login-page">
    <el-aside class="aside-section">
      <img src="../assets/loginBox.svg" class="aside-img" />
      <p class="aside-title">欢迎使用市场调研系统</p>
    </el-aside>

    <el-main class="main-section">
      <div class="form-card">
        <div class="main-title">欢迎登录</div>
        <el-form ref="loginRefForm" :model="user" label-width="80px" :rules="loginRules" class="login-form">
          <el-form-item label="账号" prop="loginAct">
            <el-input v-model.trim="user.loginAct" placeholder="请输入账号" size="large" />
          </el-form-item>
          <el-form-item label="密码" prop="loginPwd">
            <el-input type="password" v-model.trim="user.loginPwd" placeholder="请输入密码" size="large" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="login" size="large" class="login-btn">登 录</el-button>
          </el-form-item>
          <el-form-item class="remember-me">
            <el-checkbox v-model="user.rememberMe" label="记住我" size="large" />
          </el-form-item>
        </el-form>
      </div>
    </el-main>
  </el-container>
</template>

<style lang="scss" scoped>
.login-page {
  height: 100vh;
  overflow: hidden;
  background: #f0f2f5;
}

.aside-section {
  width: 45%;
  background: linear-gradient(135deg, #2c3e50, #4ca1af);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fff;
  padding: 2rem;

  .aside-img {
    width: 60%;
    max-width: 300px;
    margin-bottom: 30px;
  }

  .aside-title {
    font-size: 26px;
    font-weight: bold;
  }
}

.main-section {
  width: 50%;
  display: flex;
  align-items: center;
  justify-content: center;


  .form-card {
    background: #fff;
    padding: 3rem 2.5rem 3rem 3.5rem; // 左右不对称，右边多一点
    border-radius: 16px;
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 420px; // 稍微宽一点也能让布局更舒展
  }

  .main-title {
    text-align: center;
    font-size: 28px;
    font-weight: bold;
    margin-bottom: 30px;
    color: #333;
  }

  .login-form {
    .el-form-item {
      padding-right: 40px; // 增加右边 padding
      margin-bottom: 24px;
    }

    .login-btn {
      width: 100%;
    }

    .remember-me {
      text-align: right;
    }
  }
}
</style>
