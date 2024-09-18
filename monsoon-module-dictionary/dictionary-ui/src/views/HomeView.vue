<template>
    <header>
        <n-card :title="title" :segmented="{
            content: true,
            footer: 'soft',
        }">
            <template #header-extra>
                <n-button>
                    <template #icon>
                        <n-icon>
                            <LockOpenOutlined />
                        </n-icon>
                    </template>
                    Authorization
                </n-button>
            </template>


            <n-form ref="formRef" :model="formValue">
                <n-space vertical>
                    <n-grid cols="2 s:3 m:4 l:5 xl:6 2xl:7" responsive="screen" x-gap="10">
                        <n-grid-item>
                            <n-form-item label="姓名" path="user.name">
                                <n-input v-model:value="formValue.user.name" placeholder="输入姓名" />
                            </n-form-item>
                        </n-grid-item>
                        <n-grid-item>
                            <n-form-item label="年龄" path="user.age">
                                <n-input v-model:value="formValue.user.age" placeholder="输入年龄" />
                            </n-form-item>
                        </n-grid-item>
                        <n-grid-item>
                            <n-form-item label="电话号码" path="phone">
                                <n-input v-model:value="formValue.phone" placeholder="电话号码" />
                            </n-form-item>
                        </n-grid-item>
                    </n-grid>

                    <n-space justify="end">
                        <n-button attr-type="button" secondary type="info" @click="handleValidateClick">
                            Search
                        </n-button>
                    </n-space>
                </n-space>
            </n-form>

            <n-divider />

            <n-space vertical>
                <n-grid cols="2 m:3" responsive="screen">
                    <n-grid-item span="2 m:1">
                        <n-card title="Directory" :bordered="false">
                            <template #header-extra>
                                <n-button strong secondary type="primary">
                                    <template #icon>
                                        <n-icon>
                                            <CreateNewFolderOutlined />
                                        </n-icon>
                                    </template>
                                </n-button>
                                <div style="width: 5px;" />
                                <n-button strong secondary type="error">
                                    <template #icon>
                                        <n-icon>
                                            <DeleteOutlined />
                                        </n-icon>
                                    </template>
                                </n-button>
                            </template>

                            <n-input-group>
                                <n-input />
                                <n-button type="primary" ghost>
                                    <template #icon>
                                        <n-icon>
                                            <SearchOutlined />
                                        </n-icon>
                                    </template>
                                </n-button>
                            </n-input-group>
                            <div style="height: 10px;" />
                            <n-spin :show="menuLoading">
                                <n-tree block-line :data="tree" label-field="name" :node-props="nodeProps" selectable />
                            </n-spin>
                        </n-card>
                    </n-grid-item>

                    <n-grid-item :span="2">
                        <n-card title="Directory Details" :bordered="false">
                            <template #header-extra>
                                <n-button strong secondary type="primary">
                                    <template #icon>
                                        <n-icon>
                                            <AddOutlined />
                                        </n-icon>
                                    </template>
                                </n-button>
                                <div style="width: 5px;" />
                                <n-button strong secondary type="info">
                                    <template #icon>
                                        <n-icon>
                                            <SaveOutlined />
                                        </n-icon>
                                    </template>
                                </n-button>
                                <div style="width: 5px;" />
                            </template>
                            <n-data-table :single-line="false" :bordered="false" :columns="columns" :data="table" />
                        </n-card>
                    </n-grid-item>
                </n-grid>
            </n-space>
        </n-card>
    </header>
</template>

<script setup lang="ts">
// import { RouterLink, RouterView } from 'vue-router'
// import HelloWorld from './components/HelloWorld.vue'
import { NCard, NDataTable, NDivider, NTree, NTable, NInputNumber, NSpin, NRow, NLayout, NLayoutHeader, NFlex, NInputGroup, NIcon, NButton, NPagination, NSpace, NForm, NFormItem, NInput, NGrid, NGridItem } from 'naive-ui'
import { defineComponent, ref, h, inject, type UnwrapRef, type Ref } from 'vue'
import type { DataTableColumns, TreeOption } from 'naive-ui'
import { LockOpenOutlined, LockOutlined } from '@vicons/material'
import { AddOutlined, SaveOutlined, DeleteOutlined, SearchOutlined, CreateNewFolderOutlined } from '@vicons/material'
import { buildTree } from '../tree'
import type { ApiService } from '@/api'

const title = 'Dictionary UI'
document.title = title

const env = process.env.NODE_ENV

const menuLoading = ref(true)

const tree: Ref<UnwrapRef<TreeOption[]>> = ref([]);
const apiService = inject<ApiService>('apiService');
apiService?.get<Dictionary[]>("/directory-list").then(resp => {
    const newTree: TreeOption[] = buildTree(resp as [], 'id', 'pid', '')
    tree.value.splice(0, tree.value.length, ...newTree)
    menuLoading.value = false
}).catch(e => {
    menuLoading.value = false
})

interface Dictionary {
    id: string
    pid: string
    fullPathId: string
    seq: number
    langKey: any
    langKeyVal: any
    name: string
    description: any
    beDirectory: boolean
}

const table = ref<Dictionary[]>([])
const nodeProps = ({ option }: { option: TreeOption }) => {
    return {
        onClick() {
            apiService?.get<Dictionary[]>("/item-list", { pid: option.id }).then(resp => {
                const list: Dictionary[] = resp
                table.value.splice(0, tree.value.length, ...list)
            })
        }
    }
}

const page = ref(2)
const formValue = ref({
    user: {
        name: '',
        age: ''
    },
    phone: ''
})

function handleValidateClick(e: MouseEvent) {
    e.preventDefault()
}

interface RowData {
    key: number
    name: string
    age: number
    address: string
    tags: string[]
}

function createColumns({
    sendMail
}: {
    sendMail: (rowData: RowData) => void
}): DataTableColumns<RowData> {
    return [
        {
            title: 'Seq',
            key: 'seq',
            render(row, index) {
                return h(NInputNumber, {
                    value: table.value[index].seq,
                    onUpdateValue(v) {
                        table.value[index].seq = v || 0
                    }
                })
            }
        },
        {
            title: 'ID',
            key: 'id',
            render(row, index) {
                return h(NInput, {
                    value: table.value[index].id,
                    onUpdateValue(v) {
                        table.value[index].id = v
                    }
                })
            }
        },
        {
            title: 'Name',
            key: 'name',
            render(row, index) {
                return h(NInput, {
                    value: table.value[index].name,
                    onUpdateValue(v) {
                        table.value[index].name = v
                    }
                })
            }
        },
        {
            title: 'Language Key',
            key: 'langKey',
            render(row, index) {
                return h(NInput, {
                    value: table.value[index].langKey,
                    onUpdateValue(v) {
                        table.value[index].langKey = v
                    }
                })
            }
        },
        {
            title: 'Description',
            key: 'description',
            render(row, index) {
                return h(NInput, {
                    value: table.value[index].description,
                    onUpdateValue(v) {
                        table.value[index].description = v
                    }
                })
            }
        },
        {
            title: '',
            key: 'actions',
            render(row, index) {
                return h('div', null, [
                    h(NButton, {
                        text: true,
                        style: {
                            fontSize: '20px'
                        }
                    }, {
                        default: () => h(NIcon, {}, { default: () => h(DeleteOutlined) })
                    })
                ])
                // return h(
                //   NButton,
                //   {
                //     size: 'small',
                //     onClick: () => sendMail(row)
                //   },
                //   { default: () => 'Send Email' }
                // )
            }
        }
    ]
}

const columns = createColumns({
    sendMail(rowData) {
        //console.log(`send mail to ${rowData.name}`)
    }
});

</script>