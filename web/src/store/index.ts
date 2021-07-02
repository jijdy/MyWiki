import { createStore } from 'vuex'

declare let SessionStorage: any;
const USER = "USER";
//SessionStorage用于数据的长久存储
const store = createStore({
  state: {
    user: SessionStorage.get(USER) || {},
  },
  mutations: {
    setUser (state, user) {
      state.user = user;
      SessionStorage.set(USER, user);
    }
  },
  actions: {
  },
  modules: {
  }
});

export default store;
