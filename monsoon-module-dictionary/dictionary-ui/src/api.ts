import axios, { type AxiosInstance, AxiosError } from 'axios';
import type { NotificationType } from 'naive-ui'
import { computed, defineComponent, ref } from 'vue'
import type { ConfigProviderProps } from 'naive-ui'
import { createDiscreteApi, darkTheme, lightTheme } from 'naive-ui'
import type { NotificationApiInjection } from 'naive-ui/es/notification/src/NotificationProvider';

export interface R<T> {
  respCode: number
  respMsg: string
  data: T
}

export class ApiService {
  private instance: AxiosInstance;
  private headers: Record<string, string> = {};
  private notification: NotificationApiInjection;

  constructor() {
    const themeRef = ref<'light' | 'dark'>('light')
    const configProviderPropsRef = computed<ConfigProviderProps>(() => ({
      theme: themeRef.value === 'light' ? lightTheme : darkTheme
    }))
    this.notification = createDiscreteApi(['notification'], {
      configProviderProps: configProviderPropsRef
    }).notification;

    this.instance = axios.create({
      baseURL: '/dictionary-api', // 设置基础 URL
      timeout: 10000, // 设置请求超时时间
    });

    this.setHeaders({
      'Content-Type': 'application/json'
    });

    // 添加响应拦截器
    this.instance.interceptors.response.use(
      response => response.data,
      (error: AxiosError) => {
        this.handleError(error);
        return Promise.reject(error);
      }
    );
  }

  private handleError(error: AxiosError) {
    let errMsg: string = ''
    const resp = error.response
    if (resp) {
      const data = resp.data
      console.error('Error response:', resp.data);
      if (typeof data === 'object' && data !== null) {
        if ('respMsg' in data) {
          errMsg = String(data.respMsg)
        }
      }
    } else {
      console.error('Error message:', error.message);
    }

    if (errMsg === '') {
      errMsg = error.message;
    }
    this.notification.error({
      title: errMsg,
      duration: 5000,
      keepAliveOnHover: true
    })
  }

  // 创建资源
  public post<T>(url: string, data: T) {
    return this.instance.post(url, data);
  }

  // 读取资源
  public async get<T>(url: string, params?: any): Promise<T> {
    const response = await this.instance.get<R<T>>(url, { params });
    return response.data
  }

  // 删除资源
  public delete(url: string, params?: any) {
    return this.instance.delete(url, { params });
  }

  // 修改 headers 的方法
  public setHeaders(headers: Record<string, string>) {
    this.headers = { ...this.headers, ...headers }; // 合并新 headers
    this.instance.defaults.headers.common = this.headers; // 更新 Axios 实例的 headers
  }
}