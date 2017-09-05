<template>
  <div>
    <md-subheader>Login</md-subheader>
    <div class="section">
      <login/>
    </div>

    <md-subheader>User list</md-subheader>
    <div class="section">
      <md-layout md-gutter="20">
        <md-layout md-flex="25" v-for="user in userList" :key="user.name">
          <user-card
              :user="user"
              :editing="user.editing"
              :save="save(user)"
              :remove="remove(user)"
              :onStopEdit="onStopEdit(user)"/>
        </md-layout>
        <md-layout md-flex="25">
          <div class="new-card" v-on:click="create">
            <md-icon>add</md-icon>
          </div>
        </md-layout>
      </md-layout>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import UserCard from "./UserCard.vue";
  import Login from "./Login.vue";

  export default {
    components: {
      UserCard,
      Login
    },
    name: 'hello',
    methods: {
      create: function () {
        this.userList.push({
          _key: new Date().getTime(),
          username: "",
          email: "",
          editing: true
        });
      },
      onStopEdit: function (user) {
        return (done) => {
          if (!user._saving && user.id === undefined) {
            console.log("Cancelled", user);
            this.userList = this.userList.filter(u => u._key !== user._key);
          }
          done();
        }
      },
      save: (user) => (onSuccess, onFailure) => {
        //TODO: Show error messages to user
        if (user.id) {
          console.log("update", user.username);
          axios.put(`${process.env.API_BASE_URI}/user/${user.username}`, user).then(resp => {
            onSuccess();
          }).catch(err => {
            console.log(err);
            onFailure();
          });
        } else {
          console.log("create", user.username);
          axios.post(`${process.env.API_BASE_URI}/user`, user).then(resp => {
            user.id = resp.data.id;
            onSuccess();
          }).catch(err => {
            console.log(err);
            onFailure();
          });
        }
      },
      remove: function (user) {
        return () => {
          console.log("remove", user.username);
          axios.delete(`${process.env.API_BASE_URI}/user/${user.username}`).then(() => {
            this.userList = this.userList.filter(u => u.username !== user.username);
          });
        }
      },
    },
    data() {
      axios.get(`${process.env.API_BASE_URI}/user`).then(resp => {
        this.userList = resp.data.content;
      });

      return {
        userList: []
      }
    }
  }
</script>

<style lang="scss" scoped>
  .card {
    width: 100%;
  }

  .new-card {
    border: 4px dashed #7f7f7f;
    color: #7f7f7f;
    width: 100%;
    min-height: 225px;
    margin: 15px;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;

    .md-icon {
      width: 40px;
      height: 40px;
      font-size: 40px;
      font-weight: 700;
    }
  }

  .section {
    padding-left: 25px;
  }
</style>
