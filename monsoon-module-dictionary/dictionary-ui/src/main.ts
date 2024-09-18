import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { injectDependency } from './di'

const app = createApp(App)

injectDependency(app)
app.use(createPinia())
app.use(router)

app.mount('#app')
