import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../components/myHome.vue'
import QuestionList from '../components/admin/QuestionList.vue'

Vue.use(VueRouter)

const routes = [{
    path: "/",
    redirect: "/home", // 首页重定向
}, {
    path: "/home",
    component: Home,
    redirect: "/question",
    children: [ // 自组件

        { path: "/question", component: QuestionList, },

    ]
}]

const router = new VueRouter({
    routes
})

// 未跳转时使用
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location, onResolve, onReject) {
        if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject);
        return originalPush.call(this, location).catch(err => err);
    }
    // 挂载路由导航守卫
router.beforeEach((to, from, next) => {
    // to 将要访问
    // from 从哪访问
    // next 接着干的事,next(url) 重定向到url，空则继续访问 to 的路径
    if (to.path == '/question') return next();
    // 获取 user
    const userFlag = window.sessionStorage.getItem("user");
    // 判断是否为空
    if (!userFlag) return next('/question'); // 无值返回登陆页
    next(); // 符合要求
})

export default router