<template>
    <!--引入 container 布局-->
    <el-container class="home-container">
        <!--头部-->
        <el-header>
            <!--图标-->
            <div>
                <img src="../assets/logo.png" alt/>
                <span class="span_header">leetcode search project</span>
            </div>
        </el-header>
        <!--主体-->
        <el-container>
            <!--主体内容-->
            <el-main>
                <router-view></router-view>
            </el-main>
        </el-container>
    </el-container>
</template>
<script>
export default{
    data(){
        return{
            isCollapse:false, // 伸缩
            activePath:'/question', // 默认路径
        }
    },
    // onload 事件
    created(){
        // 查询menulist
        this.activePath = window.sessionStorage.getItem('activePath'); // 在页面加载时取出 path，并动态修改
    },
    methods:{
        // 控制伸缩
        toggleCollapse(){
            this.isCollapse = !this.isCollapse;
        },  
        // 保存路径
        saveNavState(activePath){
            window.sessionStorage.setItem("activePath",activePath); // 存放在 session 中
            this.activePath = activePath;
        }
    }
}
</script>
<style lang="less" scoped>
.home-container{
    height : 100%;
}
.el-header{
    background-color: #373d41;
    display: flex;
    justify-content: space-between;
    padding-left: 0%;
    color: #fff;
    font-size: 20px;
    align-items: center;
    div{
        display: flex;
        align-items: center;
        span{
            margin-left: 20px;
        }
    }
}
.el-aside{
    background-color: #333744;
    .el-menu{
        border-right: none;
    }
}
.el-main{
    background-color: #eaedf1;
}
img{
    width:55px;
    height: 55px;
}
.toggle-button{
    background-color: #4A5064;
    font-size: 10px;
    line-height: 24px;
    color:#fff;
    text-align: center;
    letter-spacing: 0.2em;
    cursor:pointer;
}
</style>