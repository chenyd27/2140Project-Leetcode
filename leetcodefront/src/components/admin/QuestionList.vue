<template>
    <div> 
        <!--用户列表主体部分-->
        <el-card>
            <!--搜索区域-->
            <el-row :gutter="100">
                <el-col :span="10">
                    <!--添加搜索-->
                    <el-input placeholder="please input the search query" v-model="queryInfo.query" clearable @clear="getQuestionList">
                        <el-button slot="append" icon="el-icon-search" @click="getQuestionList"></el-button>
                    </el-input>
                </el-col>
            </el-row>
            
            <el-divider></el-divider>
            <el-menu default-active="2" class="el-menu-vertical-demo">
                <el-submenu :index="item.id" v-for="item in questionList" :key="item.id" class="showResult">
                    <template slot="title">
                        <div id="container">
                            <div class="lable1">{{item.id}}</div>
                            <div class="lable2"><a :href="item.url" target="_blank" class="buttonText">{{item.title}}</a></div>
                            <div class="lable3" :style="classObje(item.difficulty)">{{item.difficulty}}</div>
                            <div class="lable4">{{item.score}}</div>
                        </div>
                    </template>
                    <!--
                    <el-menu-item-group>
                    <template slot="title">
                            <el-table :data="item.solutionList" border stripe> 
                                
                                <el-table-column type="index"></el-table-column>
                                <el-table-column label="Title" prop="title"></el-table-column>
                                <el-table-column label="Author" prop="author"></el-table-column>
                                <el-table-column label="Url" prop="url">
                                    <template slot-scope="scope">
                                        <a :href="scope.row.url" target="_blank" class="buttonText">Click to jump</a>
                                    </template>          
                                </el-table-column>
                                
                                
                                <el-table-column label="State" prop="state">
                                    <template slot-scope="scope">
                                        <el-switch v-model="scope.row.state" @change="userStateChange(scope.row)"></el-switch>
                                    </template>
                                </el-table-column>
                                
                            </el-table>
                    </template>
                    </el-menu-item-group>
                    -->
                </el-submenu>
            </el-menu>
            <!--分页组件-->
            <div>
                <!--size-change 每页最大变化数
                    current-change 当前最大变化数
                    layout 功能组件
                --> 
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryInfo.pageNum"
                    :page-size="queryInfo.pageSize"
                    layout="total, prev, pager, next"
                    :total="count">
                </el-pagination>
                
            </div>
        </el-card>
    </div>
</template>
<script>
export default {
    data(){
        return{
            // 查询信息实体
            queryInfo:{
                query:"",
                pageNum:1,
                pageSize:30,
            },
            // userList 内容
            userList:[],
            questionList:[], // questionList 内容
            // 查询数目
            count:20,
        }
    },
    created(){
        this.getQuestionList();
    },
    methods:{
        // 获取所有问题
        async getQuestionList(){
            const {data:res} = await this.$http.get("search",{params:this.queryInfo});
            this.questionList = res.list; // 问题列表数据封装
            this.count = res.size;
            console.log(res);
        },
        // 最大数目
        handleSizeChange(newSize){
            this.queryInfo.pageSize = newSize;
            this.getQuestionList(); // 获取每页的userlist
        },
        // pageNum 触发
        handleCurrentChange(newPage){
            this.queryInfo.pageNum = newPage;
            this.getQuestionList();
        },
        // 修改用户状态
        async userStateChange(userInfo){
            var formData = new FormData();
            formData.append('id',userInfo.id);
            formData.append('state',userInfo.state);
            const {data:res} = await this.$http.put('/userstate',formData);
            if(res != "success"){
                userInfo.state = !userInfo.state;
                return this.$message.error("fail");
            }
            this.$message.success("success");
        },
        classObje(difficulty) {            
            if (difficulty == "easy"){
            return {'color':'green'}  
            } else if (difficulty == "medium"){
            return {'color':'orange',}
            } else if (difficulty == "hard"){
            return {'color':'red',}
            }      
        }
    },
}
</script>
<style lang="less" scoped>
.el-breadcrumb{
    margin-bottom: 15px;
    font-size:15px;
}
.showResult{
    border: 0.1px solid rgb(240,240,240);
}
.title{
    top:10px;
}
.el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .container {
width:80%;
height: 200px;
background-color: red;
overflow: auto;
white-space: nowrap;
}
.lable1 {
width: 10%;
display: inline-block;
}
.lable2 {
width: 40%;
display: inline-block;
}
.lable3 {
width: 25%;
display: inline-block;
}
.lable4 {
width: 25%;
display: inline-block;
}

</style>