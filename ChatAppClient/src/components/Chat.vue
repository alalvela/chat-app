<template>
    <div>
        <div class="row">
            <nav class="navbar navbar-light">
            <div class="container-fluid">
                <ul class="nav navbar-nav">
                    <li><a class="nav-item" href="#">Početna</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right bg-light">
                    <li class="bg-light"><a class="bg-light" href="#">
                        <span class="glyphicon glyphicon-user"></span>
                        {{username}}</a>
                    </li>
                    <li><a v-on:click="sendLogoutData" href="#"><span class="glyphicon glyphicon-log-out"></span>
                        Logout</a></li>
                </ul>
            </div>
            </nav>
        </div>
    <div class="row">
    <div class="col-md-1"></div>    

    <div class="col-md-8">    
    <div class="container">
    <div class="row">
        <div class="col-md-10">
            <div class="panel panel-primary">
                <div class="panel-heading" id="accordion">
                    <span class="glyphicon glyphicon-comment"></span> {{activeConversationUser}}
                </div>
            <div>
                
                <div class="panel-body col-md-12">
                    <ul class="chat">
                        <template v-for="chatMessage in chatHistory">
                            <template v-if="chatMessage.sender === username">
                                <li class="right clearfix">
                                    <span class="chat-img pull-right">
                                        <img src="http://placehold.it/50/FA6F57/fff&text=ME" alt="User Avatar" class="img-circle" />
                                    </span>
                                    <div class="chat-body clearfix row">
                                        <div class="col-md-3"></div>
                                        <div class="col-md-6">
                                            <h5>
                                                {{chatMessage.content}}
                                            </h5>
                                        </div>
                                        <div class="header col-md-3">
                                            <div class="row">
                                                <h4 class="pull-right primary-font">{{chatMessage.sender}}</h4> 
                                            </div>
                                            <div class="row">
                                                <small class="pull-right text-muted">
                                                <span class="glyphicon glyphicon-time"></span>{{chatMessage.time}}</small>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            </template>
                            <template v-else>
                                <li class="left clearfix">
                                    <span class="chat-img pull-left">
                                        <img src="http://placehold.it/50/55C1E7/fff&text=U" alt="User Avatar" class="img-circle" />
                                    </span>
                                    <div class="chat-body clearfix row">
                                        <div class="header col-md-2">
                                            <div class="row">
                                                <h4 class="pull-left primary-font">{{chatMessage.sender}}</h4>
                                            </div>
                                            <div class="row">
                                                <small class="pull-left text-muted">
                                                <span class="glyphicon glyphicon-time"></span>{{chatMessage.time}}</small>
                                            </div>
                                        </div>
                                        <div class="col-md-6 bg-white">
                                            <h5>
                                                {{chatMessage.content}}
                                            </h5>
                                        </div>
                                    </div>
                                    <div class="col-md-4"></div>
                                </li>
                            </template>                              
                        </template>
                    </ul>
                </div>
                <div v-if="activeConversationUser !== ''" class="panel-footer">
                <!-- <div class="panel-footer"> -->
                
                    <div class="row">
                        <input v-on:keyup.enter="sendMessage" v-model="message"  type="text" class="form-control input-sm" placeholder="Type your message here..." />
                    </div>
                </div>
            </div>
            </div>
        </div>
    </div>
</div>
    </div>
    <div class="col-md-2">
        <div class="row">
            <div class="panel panel-primary">
                <div class="panel-heading" id="accordion">
                    <span class="glyphicon glyphicon-comment"></span> Active users
                </div>
                <div class="panel-body">
                    <div class="table-container">
                            <table class="table table-filter">
                                <tbody>
                                    <tr v-for="user in activeUsers" >
                                        <td>
                                            <div class="media">
                                                <span class="glyphicon glyphicon-envelope pull-left"></span>
                                                <a @click="openChat(user.username)" href="#" >
                                                   {{user.username}}
                                                </a>           
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                    </div>             
                </div>
            </div>
        </div>
        <div class="row">
            <div class="panel panel-primary">
                <div class="panel-heading" id="accordion">
                    <span class="glyphicon glyphicon-user"></span> Groups
                </div>
                <div class="panel-body">
                    <div class="table-container">
                            <table class="table table-filter">
                                <tbody>
                                    <tr v-for="group in groups" >
                                        <td>
                                            <div class="media">
                                                <span class="glyphicon glyphicon-user pull-left"></span>
                                                <a @click="openGroupChat(group.name)" href="#" >
                                                   {{group.name}}
                                                </a>           
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                    </div>             
                </div>
            </div>
        </div>
    </div>
</div>
</div>

</template>

<script>
    export default {
        name: 'Chat',
        data() {
            return {
                username : '',
                activeConversationUser : '',
                activeUsers : [],
                message: '',
                chatHistory: [],
                groups: [],
                isGroup : false
            }
        },
        methods: {
            addMessageToActiveChat: function(data) {
                var fetched = JSON.parse(data);
                var msg = fetched.object;
                if (msg.sender === this.activeConversationUser || msg.reciever === this.activeConversationUser) {
                    this.chatHistory.push(msg);
                }
            },
            sendMessage: function() {
                var data = JSON.stringify({
                    "action" : "sendMessage",
                    "username" : this.username,
                    "data" : { 
                        "username" : this.activeConversationUser,
                        "messageContent" : this.message,
                        "isGroup" : this.isGroup
                    }
                });
                window.socket.send(data);
                this.message = '';
            },
            sendMessageComplete: function(data) {
            },
            openGroupChat: function(groupName) {
                this.activeConversationUser = groupName;
                this.isGroup = true;

                var data = JSON.stringify({
                    "action" : "fetchGroupConversation",
                    "username" : this.username,     
                    "data" : { 
                        "username" : groupName              //umjesto username saljes ime grupe
                    }
                });
                window.socket.send(data);
                //iskrcaj poruke
            },
            openGroupChatComplete: function(data) {
                var fetched = JSON.parse(data);
                this.chatHistory = fetched.object;
            },
            openChat: function(user) {
                this.activeConversationUser = user;
                this.isGroup = false;
                var data = JSON.stringify({
                    "action" : "fetchConversation",
                    "username" : this.username,
                    "data" : { 
                        "username" : user
                    }
                });
                window.socket.send(data);
            },
            openChatComplete: function(data) {
                var fetched = JSON.parse(data);
                this.chatHistory = fetched.object;
            },
            populateData: function(data) {
                var fetched = JSON.parse(data);
                var retObj = fetched.object;
                this.username = retObj.loggedInUsername;
                this.activeUsers = retObj.activeUsers;
                this.groups = retObj.groups;
            },
            sendPopulateDataRequest: function() {
                var data = JSON.stringify({
                    "action" : "fetchChatData",
                    "username" : "",
                    "data" : { } 
                });
                window.socket.send(data);
            },
            addUserToActive: function(data) {
                var fetched = JSON.parse(data);
                var retObj = fetched.object;
                this.activeUsers.push(retObj);
            },
            removeUserFromActive: function(data) {
                var fetched = JSON.parse(data);
                var retObj = fetched.object;
                this.activeUsers = this.activeUsers.filter(function(el) {
                    return el.username !== retObj.username;
                });
            },
            sendLogoutData: function() {
                var data = JSON.stringify({
                    "action" : "logout",
                    "username" : "",
                    "data" : {
                        "username" : this.username
                    } 
                });
                window.socket.send(data);
            },
            logoutComplete: function(data) {
                this.$router.push('/login');
            }
        },
        created: function() {
            this.sendPopulateDataRequest();
            this.$parent.$on('logout', this.logoutComplete);
            this.$parent.$on('fetchChatData', this.populateData);
            this.$parent.$on('addActiveUser', this.addUserToActive);
            this.$parent.$on('removeActiveUser', this.removeUserFromActive);
            this.$parent.$on('fetchConversation', this.openChatComplete);
            this.$parent.$on('addMessageToActiveChat', this.addMessageToActiveChat);
            this.$parent.$on('fetchGroupConversation', this.openGroupChatComplete);

        }
    }
    
</script>

<style>
.chat
{
    list-style: none;
    margin: 0;
    padding: 0;
}

.chat li
{
    margin-bottom: 10px;
    padding-bottom: 5px;
    border-bottom: 1px dotted #B3A9A9;
}

.chat li.left .chat-body
{
    margin-left: 60px;
}

.chat li.right .chat-body
{
    margin-right: 60px;
}


.chat li .chat-body p
{
    margin: 0;
    color: #777777;
}

.panel .slidedown .glyphicon, .chat .glyphicon
{
    margin-right: 5px;
}

.panel-body
{
    overflow-y: scroll;
    height: 250px;
}

::-webkit-scrollbar-track
{
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
    background-color: #F5F5F5;
}

::-webkit-scrollbar
{
    width: 12px;
    background-color: #F5F5F5;
}

::-webkit-scrollbar-thumb
{
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
    background-color: #555;
}
    

</style>