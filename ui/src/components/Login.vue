<template>
  <form v-on:submit.prevent="doLogin">
    <md-layout md-gutter="10" md-row style="height: 20px;">

      <md-layout md-flex="10">
        <md-input-container>
          <label>Username</label>
          <md-input v-model="creds.username" required></md-input>
        </md-input-container>
      </md-layout>

      <md-layout md-flex="10">
        <md-input-container>
          <label>Password</label>
          <md-input type="password" v-model="creds.password" required></md-input>
        </md-input-container>
      </md-layout>

      <md-layout md-flex="10">
        <md-button type="submit" class="md-primary md-raised">Login</md-button>
        <md-icon class="succeed" v-if="success === true">check_circle</md-icon>
        <md-icon class="fail" v-if="success === false">error</md-icon>
      </md-layout>
    </md-layout>
  </form>
</template>

<script>
  import axios from "axios";

  export default {
    name: 'login',
    methods: {
      doLogin: function () {
        axios.post(`${process.env.API_BASE_URI}/login`, this.creds).then(resp => {
          this.success = true;
        }).catch(resp => {
          this.success = false;
        });
      }
    },
    data() {
      return {
        creds: {},
        success: undefined
      }
    }
  }
</script>

<style lang="scss" scoped>
  .fail {
    color: #c61e18;
  }

  .succeed {
    color: #1d9521;
  }

  .fail, .succeed {
    position: relative;
    top: 5px;
  }

  form {
    margin: 10px 10px 30px;

    .md-input-container {
      position: relative;
      top: -16px;
      margin-right: 10px;

      label {
        font-size: 12px;
      }
    }

    .md-button {
      position: relative;
      top: -6px;
    }
  }

</style>