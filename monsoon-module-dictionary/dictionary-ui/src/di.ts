import type { App } from "vue";
import { ApiService } from './api'

export function injectDependency(app: App<Element>) {
    app.provide('apiService', new ApiService());
}