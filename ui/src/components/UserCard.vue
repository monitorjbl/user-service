<template>
  <md-card class="user-card">
    <div class="icon hidden" v-if="!isEditing">
      <span v-on:click="startEdit"><md-icon class="md-primary">edit</md-icon></span>
      <span v-on:click="remove"><md-icon class="md-primary" v-if="remove != undefined">delete</md-icon></span>
    </div>
    <div class="icon">
      <span v-on:click="stopEdit" v-if="isEditing"><md-icon class="md-primary">cancel</md-icon></span>
    </div>

    <md-card-media>
      <img :src="avatarImage" alt="Avatar">
    </md-card-media>
    <md-card-header>
      <div class="md-title" v-if="!isEditing">{{ user.username }}</div>
      <div class="md-subhead" v-if="!isEditing">{{ user.email }}</div>
      <div class="md-subhead editor" v-if="isEditing">
        <form v-on:submit.prevent="doSave">
          <md-input-container>
            <label>Username</label>
            <md-input v-model="user.username" required></md-input>
          </md-input-container>
          <md-input-container>
            <label>Email</label>
            <md-input type="email" v-model="user.email" required></md-input>
          </md-input-container>
          <md-input-container>
            <label>Password</label>
            <md-input type="password" v-model="user.password" required></md-input>
          </md-input-container>
          <md-button type="submit">Save</md-button>
        </form>
      </div>
    </md-card-header>
  </md-card>
</template>

<script>

  export default {
    name: 'user-card',
    props: {
      user: {
        type: Object
      },
      editing: {
        type: Boolean
      },
      save: {
        type: Function
      },
      onStopEdit: {
        type: Function,
        default: (done) => done()
      },
      remove: {
        type: Function
      }
    },
    data() {
      return {
        isEditing: this.editing,
        startEdit: () => {
          this.isEditing = true;
        },
        stopEdit: () => {
          this.onStopEdit(() => this.isEditing = false);
        },
        doSave: () => {
          //TODO: form field validation
          this.user._saving = true;
          this.save(() => this.stopEdit());
        },
        avatarImage: this.user.avatar === undefined ? `http://lorempixel.com/500/200/?&_time=${Math.random()}` : this.user.avatar
      }
    }
  }
</script>

<style lang="scss" scoped>
  @keyframes fadein {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }

  .user-card {
    margin: 15px;

    .icon {
      position: absolute;
      top: 10px;
      right: 5px;
      z-index: 10;
      width: 60px;
      text-align: right;

      .md-icon {
        font-size: 20px;
        color: #d0d0d0;
        cursor: pointer;
      }
    }

    .icon.hidden {
      display: none;
    }

    .md-card-media {
      width: 390px;
      height: 156px;
      background-image: url(../assets/blurry-placeholder.jpg);

      img {
        animation: fadein 2s;
      }
    }
  }

  .user-card:hover {
    .icon {
      display: inline-block;
    }
  }
</style>
