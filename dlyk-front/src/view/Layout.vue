<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { storeToRefs } from 'pinia'
import useUserStore from '../store/index.js'
import { logoutApi } from '../api/user.js'

const isCollapse = ref(false)
const userStore = useUserStore()
const { userInfo } = storeToRefs(userStore)
const router = useRouter()

const logout = async () => {
  await ElMessageBox.confirm('确定要退出吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await logoutApi()
    userStore.clearUserInfo()
    ElMessage.success('退出成功')
    await router.push('/login')
  })
}

const getRoutePath = (path) => {
  const pathArray = path.split('/')
  if (pathArray.length > 2) {
    return pathArray.slice(0, 2).join('/')
  }
  return path
}
</script>

<template>
  <el-container class="layout-container">
    <!-- 左侧导航 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="layout-aside">
      <div class="aside-title" @click="router.push('/')">
        <el-icon><Grid /></el-icon>
        <span v-show="!isCollapse">市场调研系统</span>
      </div>

      <el-menu
          :default-active="getRoutePath($route.path)"
          background-color="#1f2d3d"
          text-color="#bfcbd9"
          active-text-color="#ffd04b"
          :collapse="isCollapse"
          :collapse-transition="false"
          :unique-opened="true"
          :router="true"
          class="aside-menu"
      >
        <!-- 动态子菜单 -->
        <el-sub-menu
            :index="index + 1"
            v-for="(menu, index) in userInfo.menuPermissionList"
            :key="menu.id"
        >
          <template #title>
            <el-icon><component :is="menu.icon" /></el-icon>
            <span>{{ menu.name }}</span>
          </template>

          <el-menu-item
              v-for="sub in menu.subPermissionList"
              :index="sub.url"
              :key="sub.id"
          >
            <el-icon><component :is="sub.icon" /></el-icon>
            {{ sub.name }}
          </el-menu-item>
        </el-sub-menu>

        <!-- 固定菜单项 -->
        <el-menu-item index="/chatAI">
          <el-icon><ChatDotRound /></el-icon>
          <span>ChatHelp</span>
        </el-menu-item>
        <el-menu-item index="/ChatAIMorePlatForm">
          <el-icon><MessageBox /></el-icon>
          <span>Chat多选择</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主体区域 -->
    <el-container class="main-container">
      <!-- 顶部 -->
      <el-header class="layout-header">
        <el-icon @click="isCollapse = !isCollapse" class="collapse-btn"><Fold /></el-icon>

        <el-dropdown class="user-dropdown" trigger="click">
          <span class="user-info">
            {{ userStore.userInfo.name }}
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>我的资料</el-dropdown-item>
              <el-dropdown-item>修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <!-- 内容区 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>

      <!-- 底部 -->
      <el-footer class="layout-footer">
        Copyright © 2025 - 2099 市场调研系统. All Rights Reserved.
      </el-footer>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
  background-color: #f0f2f5;
}

.layout-aside {
  background: linear-gradient(to bottom, #1f2d3d, #273849);
  transition: width 0.3s;
  color: white;
  .aside-title {
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
    color: #fff;
    font-size: 16px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    cursor: pointer;
    span {
      margin-left: 10px;
    }
  }
}

.aside-menu {
  border-right: none;
}

.main-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.layout-header {
  height: 50px;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .collapse-btn {
    font-size: 20px;
    cursor: pointer;
    transition: 0.2s;
    &:hover {
      color: #409eff;
    }
  }

  .user-dropdown .user-info {
    cursor: pointer;
    font-weight: 500;
    color: #333;
    display: flex;
    align-items: center;
    gap: 5px;
  }
}

.layout-main {
  padding: 20px;
  background: #f5f7fa;
  flex-grow: 1;
  overflow-y: auto;
}

.layout-footer {
  text-align: center;
  height: 40px;
  line-height: 40px;
  background: #ffffff;
  color: #666;
  font-size: 13px;
  border-top: 1px solid #eaeaea;
}
</style>
