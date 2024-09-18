import type { TreeOption } from 'naive-ui'


export function buildTree(data: [], idName: string, pidName: string, rootVal: string): TreeOption[] {
    const map: { [key: string]: TreeOption } = {};
    const roots: TreeOption[] = [];

    // 将每个项映射到一个树节点
    data.forEach((item: Record<string, any>) => {
        const id = item[idName]
        map[id] = { key: id, children: [], ...item };
    });

    // 构建树形结构
    data.forEach(item => {
        const id = item[idName]
        const pid = item[pidName]
        if (pid === rootVal) {
            // 根节点
            roots.push(map[id]);
        } else {
            // 找到父节点并将当前项添加为子节点
            const parent = map[pid];
            if (parent) {
                parent.children!.push(map[id]);
            }
        }
    });

    return roots;
}