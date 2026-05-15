/**
 * Sidebar CRUD tables for VelocityDesk (uses API from host page).
 */
function registerEntityCrud(API) {
  function formatCellDate(value) {
    if (value == null || value === '') return '—';
    try {
      const d = new Date(value);
      if (Number.isNaN(d.getTime())) return String(value);
      return d.toLocaleString();
    } catch {
      return '—';
    }
  }

  function parseLongList(raw) {
    if (raw == null || String(raw).trim() === '') return [];
    return String(raw)
      .split(/[\s,]+/)
      .map((s) => parseInt(s, 10))
      .filter((n) => !Number.isNaN(n));
  }

  const TABLE_VIEW_KEYS = [
    'organizations',
    'roles',
    'permissions',
    'admin-users',
    'projects',
    'teams',
    'sprints',
    'workflows',
    'task-types',
    'labels',
    'tasks',
    'kanban'
  ];

  const ENTITY_CONFIG = {
    organizations: {
      apiPath: '/organizations',
      tableBodyId: 'orgTableBody',
      title: 'Organization',
      emptyCols: 4,
      rowBuilder(row) {
        return [row.name ?? '', row.slug ?? '', formatCellDate(row.createdOn)];
      },
      fields: [
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'slug', label: 'Slug', type: 'text', required: true }
      ],
      toCreatePayload(v) {
        return { name: v.name.trim(), slug: v.slug.trim() };
      },
      toUpdatePayload(v, id) {
        return { id: Number(id), name: v.name.trim(), slug: v.slug.trim() };
      }
    },
    roles: {
      apiPath: '/roles',
      tableBodyId: 'rolesTableBody',
      title: 'Role',
      emptyCols: 4,
      rowBuilder(row) {
        const ids = Array.isArray(row.permissionsDescription) ? row.permissionsDescription.join(', ') : '';
        return [row.name ?? '', ids, formatCellDate(row.createdOn)];
      },
      fields: [
        { key: 'name', label: 'Name', type: 'text', required: true },
        {
          key: 'permissionIds',
          label: 'Permissions (multi-select)',
          type: 'multiselect',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/permissions');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((p) => ({
              value: p.id,
              label: p.permissionKey + (p.description ? ` (${p.description})` : '')
            }));
          }
        }
      ],
      toCreatePayload(v) {
        return { name: v.name.trim(), permissionIds: v.permissionIds };
      },
      toUpdatePayload(v, id) {
        return { id: Number(id), name: v.name.trim(), permissionIds: v.permissionIds };
      }
    },
    permissions: {
      apiPath: '/permissions',
      tableBodyId: 'permissionsTableBody',
      title: 'Permission',
      emptyCols: 3,
      canEdit: false,
      canDelete: false,
      rowBuilder(row) {
        return [row.permissionKey ?? '', row.description ?? '', formatCellDate(row.createdOn)];
      },
      fields: [
        { key: 'permissionKey', label: 'Permission key (e.g. TASK_READ)', type: 'text', required: true },
        { key: 'description', label: 'Description', type: 'text', optional: true }
      ],
      toCreatePayload(v) {
        const desc =
          v.description != null && String(v.description).trim() !== '' ? String(v.description).trim() : null;
        return { permissionKey: v.permissionKey.trim(), description: desc };
      },
      toUpdatePayload(v, id) {
        const desc =
          v.description != null && String(v.description).trim() !== '' ? String(v.description).trim() : null;
        return { id: Number(id), permissionKey: v.permissionKey.trim(), description: desc };
      }
    },
    'admin-users': {
      apiPath: '/admin-user',
      tableBodyId: 'adminUsersTableBody',
      title: 'Admin user',
      emptyCols: 6,
      async enrichRows(list, API) {
        try {
          const [rolesData, orgsData] = await Promise.all([
            API.request('/roles').catch(() => ({})),
            API.request('/organizations').catch(() => ({}))
          ]);
          const roles = Array.isArray(rolesData.dataList) ? rolesData.dataList : [];
          const orgs = Array.isArray(orgsData.dataList) ? orgsData.dataList : [];
          const roleMap = new Map(roles.map((r) => [r.id, r.name]));
          const orgMap = new Map(orgs.map((o) => [o.id, o.name]));
          list.forEach((row) => {
            if (row.roleId != null) {
              row.roleName = roleMap.get(row.roleId) || `Role ${row.roleId}`;
            }
            if (row.organizationId != null) {
              row.organizationName = orgMap.get(row.organizationId) || `Org ${row.organizationId}`;
            }
          });
        } catch (e) {
          console.error('Failed to enrich rows', e);
        }
      },
      rowBuilder(row) {
        return [
          row.name ?? '',
          row.email ?? '',
          row.status ?? '',
          row.roleName ?? (row.roleId != null ? String(row.roleId) : ''),
          row.organizationName ?? (row.organizationId != null ? String(row.organizationId) : '')
        ];
      },
      fields: [
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'email', label: 'Email', type: 'text', required: true },
        { key: 'password', label: 'Password', type: 'password', required: true, createOnly: true },
        {
          key: 'roleId',
          label: 'Role',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/roles');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list
              .filter((r) => r.name && r.name.toLowerCase().includes('admin'))
              .map((r) => ({ value: r.id, label: r.name }));
          }
        },
        {
          key: 'organizationId',
          label: 'Organization',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/organizations');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((o) => ({ value: o.id, label: o.name + (o.slug ? ` (${o.slug})` : '') }));
          }
        }
      ],
      editFields: [
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'email', label: 'Email', type: 'text', required: true },
        { key: 'status', label: 'Status', type: 'text', required: true },
        {
          key: 'roleId',
          label: 'Role',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/roles');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list
              .filter((r) => r.name && r.name.toLowerCase().includes('admin'))
              .map((r) => ({ value: r.id, label: r.name }));
          }
        },
        {
          key: 'organizationId',
          label: 'Organization',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/organizations');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((o) => ({ value: o.id, label: o.name + (o.slug ? ` (${o.slug})` : '') }));
          }
        }
      ],
      toCreatePayload(v) {
        return {
          name: v.name.trim(),
          email: v.email.trim(),
          password: v.password,
          roleId: Number(v.roleId),
          organizationId: Number(v.organizationId)
        };
      },
      toUpdatePayload(v, id) {
        return {
          id: Number(id),
          name: v.name.trim(),
          email: v.email.trim(),
          status: v.status.trim(),
          roleId: Number(v.roleId),
          organizationId: Number(v.organizationId)
        };
      }
    },
    teams: {
      apiPath: '/member-user',
      tableBodyId: 'teamsTableBody',
      title: 'Team member',
      emptyCols: 6,
      async enrichRows(list, API) {
        try {
          const [rolesData, orgsData] = await Promise.all([
            API.request('/roles').catch(() => ({})),
            API.request('/organizations').catch(() => ({}))
          ]);
          const roles = Array.isArray(rolesData.dataList) ? rolesData.dataList : [];
          const orgs = Array.isArray(orgsData.dataList) ? orgsData.dataList : [];
          const roleMap = new Map(roles.map((r) => [r.id, r.name]));
          const orgMap = new Map(orgs.map((o) => [o.id, o.name]));
          list.forEach((row) => {
            if (row.roleId != null) {
              row.roleName = roleMap.get(row.roleId) || `Role ${row.roleId}`;
            }
            if (row.organizationId != null) {
              row.organizationName = orgMap.get(row.organizationId) || `Org ${row.organizationId}`;
            }
          });
        } catch (e) {
          console.error('Failed to enrich rows', e);
        }
      },
      rowBuilder(row) {
        return [
          row.name ?? '',
          row.email ?? '',
          row.status ?? '',
          row.roleName ?? (row.roleId != null ? String(row.roleId) : ''),
          row.organizationName ?? (row.organizationId != null ? String(row.organizationId) : '')
        ];
      },
      fields: [
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'email', label: 'Email', type: 'text', required: true },
        { key: 'password', label: 'Password', type: 'password', required: true, createOnly: true },
        {
          key: 'roleId',
          label: 'Role',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/roles');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list
              .filter((r) => r.name && !r.name.toLowerCase().includes('admin'))
              .map((r) => ({ value: r.id, label: r.name }));
          }
        },
        {
          key: 'organizationId',
          label: 'Organization',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/organizations');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((o) => ({ value: o.id, label: o.name + (o.slug ? ` (${o.slug})` : '') }));
          }
        }
      ],
      editFields: [
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'email', label: 'Email', type: 'text', required: true },
        { key: 'status', label: 'Status', type: 'text', required: true },
        {
          key: 'roleId',
          label: 'Role',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/roles');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list
              .filter((r) => r.name && !r.name.toLowerCase().includes('admin'))
              .map((r) => ({ value: r.id, label: r.name }));
          }
        },
        {
          key: 'organizationId',
          label: 'Organization',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/organizations');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((o) => ({ value: o.id, label: o.name + (o.slug ? ` (${o.slug})` : '') }));
          }
        }
      ],
      toCreatePayload(v) {
        return {
          name: v.name.trim(),
          email: v.email.trim(),
          password: v.password,
          roleId: Number(v.roleId),
          organizationId: Number(v.organizationId)
        };
      },
      toUpdatePayload(v, id) {
        return {
          id: Number(id),
          name: v.name.trim(),
          email: v.email.trim(),
          status: v.status.trim(),
          roleId: Number(v.roleId),
          organizationId: Number(v.organizationId)
        };
      }
    },
    projects: {
      apiPath: '/projects',
      tableBodyId: 'projectsTableBody',
      title: 'Project',
      emptyCols: 6,
      async enrichRows(list, API) {
        try {
          const orgsData = await API.request('/organizations').catch(() => ({}));
          const orgs = Array.isArray(orgsData.dataList) ? orgsData.dataList : [];
          const orgMap = new Map(orgs.map((o) => [o.id, o.name]));
          list.forEach((row) => {
            if (row.organizationId != null) {
              row.organizationName = orgMap.get(row.organizationId) || `Org ${row.organizationId}`;
            }
          });
        } catch (e) {
          console.error('Failed to enrich rows', e);
        }
      },
      rowBuilder(row) {
        return [
          row.name ?? '',
          row.slug ?? '',
          row.organizationName ?? (row.organizationId != null ? String(row.organizationId) : ''),
          row.status ?? '',
          row.visibility ?? ''
        ];
      },
      fields: [
        {
          key: 'organizationId',
          label: 'Organization',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/organizations');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((o) => ({ value: o.id, label: o.name + (o.slug ? ` (${o.slug})` : '') }));
          }
        },
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'slug', label: 'Slug', type: 'text', required: true },
        {
          key: 'status',
          label: 'Status',
          type: 'select',
          required: true,
          defaultValue: 'ACTIVE',
          options: [
            { value: 'ACTIVE', label: 'Active' },
            { value: 'INACTIVE', label: 'Inactive' },
            { value: 'PENDING', label: 'Pending' }
          ]
        },
        {
          key: 'visibility',
          label: 'Visibility',
          type: 'select',
          required: true,
          defaultValue: 'PRIVATE',
          options: [
            { value: 'PRIVATE', label: 'Private' },
            { value: 'PUBLIC', label: 'Public' }
          ]
        }
      ],
      toCreatePayload(v) {
        return {
          organizationId: Number(v.organizationId),
          name: v.name.trim(),
          slug: v.slug.trim(),
          status: v.status.trim(),
          visibility: v.visibility.trim()
        };
      },
      toUpdatePayload(v, id) {
        return {
          id: Number(id),
          organizationId: Number(v.organizationId),
          name: v.name.trim(),
          slug: v.slug.trim(),
          status: v.status.trim(),
          visibility: v.visibility.trim()
        };
      }
    },
    sprints: {
      apiPath: '/sprints',
      tableBodyId: 'sprintsTableBody',
      title: 'Sprint',
      emptyCols: 7,
      async enrichRows(list, API) {
        try {
          const [projectsData, workflowsData] = await Promise.all([
            API.request('/projects').catch(() => ({})),
            API.request('/workflows').catch(() => ({}))
          ]);
          const projects = Array.isArray(projectsData.dataList) ? projectsData.dataList : [];
          const workflows = Array.isArray(workflowsData.dataList) ? workflowsData.dataList : [];
          const projectMap = new Map(projects.map((p) => [p.id, p.name]));
          const workflowMap = new Map(workflows.map((w) => [w.id, w.name]));
          list.forEach((row) => {
            if (row.projectId != null) {
              row.projectName = projectMap.get(row.projectId) || `Project ${row.projectId}`;
            }
            if (row.workflowId != null) {
              row.workflowName = workflowMap.get(row.workflowId) || `Workflow ${row.workflowId}`;
            }
          });
        } catch (e) {
          console.error('Failed to enrich rows', e);
        }
      },
      rowBuilder(row) {
        return [
          row.name ?? '',
          row.projectName ?? (row.projectId != null ? String(row.projectId) : ''),
          row.workflowName ?? (row.workflowId != null ? String(row.workflowId) : ''),
          row.capacityHours != null ? String(row.capacityHours) : '',
          row.plannedStoryPoints != null ? String(row.plannedStoryPoints) : '',
          row.completedStoryPoints != null ? String(row.completedStoryPoints) : ''
        ];
      },
      fields: [
        {
          key: 'projectId',
          label: 'Project',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/projects');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((p) => ({ value: p.id, label: p.name + (p.slug ? ` (${p.slug})` : '') }));
          }
        },
        {
          key: 'workflowId',
          label: 'Workflow',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/workflows');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((w) => ({ value: w.id, label: w.name + (w.workflowType ? ` (${w.workflowType})` : '') }));
          }
        },
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'capacityHours', label: 'Capacity hours', type: 'number', required: true },
        { key: 'plannedStoryPoints', label: 'Planned story points', type: 'number', required: true },
        { key: 'completedStoryPoints', label: 'Completed story points', type: 'number', required: true }
      ],
      toCreatePayload(v) {
        return {
          projectId: Number(v.projectId),
          workflowId: Number(v.workflowId),
          name: v.name.trim(),
          capacityHours: Number(v.capacityHours),
          plannedStoryPoints: Number(v.plannedStoryPoints),
          completedStoryPoints: Number(v.completedStoryPoints)
        };
      },
      toUpdatePayload(v, id) {
        return {
          id: Number(id),
          projectId: Number(v.projectId),
          workflowId: Number(v.workflowId),
          name: v.name.trim(),
          capacityHours: Number(v.capacityHours),
          plannedStoryPoints: Number(v.plannedStoryPoints),
          completedStoryPoints: Number(v.completedStoryPoints)
        };
      }
    },
    workflows: {
      apiPath: '/workflows',
      tableBodyId: 'workflowsTableBody',
      title: 'Workflow',
      emptyCols: 5,
      async enrichRows(list, API) {
        try {
          const projectsData = await API.request('/projects').catch(() => ({}));
          const projects = Array.isArray(projectsData.dataList) ? projectsData.dataList : [];
          const projectMap = new Map(projects.map((p) => [p.id, p.name]));
          list.forEach((row) => {
            if (row.projectId != null) {
              row.projectName = projectMap.get(row.projectId) || `Project ${row.projectId}`;
            }
          });
        } catch (e) {
          console.error('Failed to enrich rows', e);
        }
      },
      rowBuilder(row) {
        return [
          row.name ?? '',
          row.workflowType ?? '',
          row.projectName ?? (row.projectId != null ? String(row.projectId) : ''),
          row.reusable === true ? 'Yes' : row.reusable === false ? 'No' : ''
        ];
      },
      fields: [
        {
          key: 'projectId',
          label: 'Project',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/projects');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((p) => ({ value: p.id, label: p.name + (p.slug ? ` (${p.slug})` : '') }));
          }
        },
        { key: 'workflowType', label: 'Workflow type', type: 'text', required: true },
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'reusable', label: 'Reusable', type: 'checkbox' }
      ],
      editFields: [
        { key: 'workflowType', label: 'Workflow type', type: 'text', required: true },
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'reusable', label: 'Reusable', type: 'checkbox' }
      ],
      toCreatePayload(v) {
        return {
          projectId: Number(v.projectId),
          workflowType: v.workflowType.trim(),
          name: v.name.trim(),
          reusable: Boolean(v.reusable)
        };
      },
      toUpdatePayload(v, id) {
        return {
          id: Number(id),
          workflowType: v.workflowType.trim(),
          name: v.name.trim(),
          reusable: Boolean(v.reusable)
        };
      }
    },
    'task-types': {
      apiPath: '/task-types',
      tableBodyId: 'taskTypesTableBody',
      title: 'Task type',
      emptyCols: 6,
      async enrichRows(list, API) {
        try {
          const [projectsData, workflowsData] = await Promise.all([
            API.request('/projects').catch(() => ({})),
            API.request('/workflows').catch(() => ({}))
          ]);
          const projects = Array.isArray(projectsData.dataList) ? projectsData.dataList : [];
          const workflows = Array.isArray(workflowsData.dataList) ? workflowsData.dataList : [];
          const projectMap = new Map(projects.map((p) => [p.id, p.name]));
          const workflowMap = new Map(workflows.map((w) => [w.id, w.name]));
          list.forEach((row) => {
            if (row.projectId != null) {
              row.projectName = projectMap.get(row.projectId) || `Project ${row.projectId}`;
            }
            if (row.workflowId != null) {
              row.workflowName = workflowMap.get(row.workflowId) || `Workflow ${row.workflowId}`;
            }
          });
        } catch (e) {
          console.error('Failed to enrich rows', e);
        }
      },
      rowBuilder(row) {
        return [
          row.name ?? '',
          row.icon ?? '',
          row.color ?? '',
          row.projectName ?? (row.projectId != null ? String(row.projectId) : ''),
          row.workflowName ?? (row.workflowId != null ? String(row.workflowId) : '')
        ];
      },
      fields: [
        {
          key: 'projectId',
          label: 'Project',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/projects');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((p) => ({ value: p.id, label: p.name + (p.slug ? ` (${p.slug})` : '') }));
          }
        },
        {
          key: 'workflowId',
          label: 'Workflow',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/workflows');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((w) => ({ value: w.id, label: w.name + (w.workflowType ? ` (${w.workflowType})` : '') }));
          }
        },
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'icon', label: 'Icon (emoji or text)', type: 'text', required: true },
        { key: 'color', label: 'Color (hex or name)', type: 'text', required: true }
      ],
      toCreatePayload(v) {
        return {
          projectId: Number(v.projectId),
          workflowId: Number(v.workflowId),
          name: v.name.trim(),
          icon: v.icon.trim(),
          color: v.color.trim()
        };
      },
      toUpdatePayload(v, id) {
        return {
          id: Number(id),
          projectId: Number(v.projectId),
          workflowId: Number(v.workflowId),
          name: v.name.trim(),
          icon: v.icon.trim(),
          color: v.color.trim()
        };
      }
    },
    labels: {
      apiPath: '/labels',
      tableBodyId: 'labelsTableBody',
      title: 'Label',
      emptyCols: 4,
      async enrichRows(list, API) {
        try {
          const projectsData = await API.request('/projects').catch(() => ({}));
          const projects = Array.isArray(projectsData.dataList) ? projectsData.dataList : [];
          const projectMap = new Map(projects.map((p) => [p.id, p.name]));
          list.forEach((row) => {
            if (row.projectId != null) {
              row.projectName = projectMap.get(row.projectId) || `Project ${row.projectId}`;
            }
          });
        } catch (e) {
          console.error('Failed to enrich rows', e);
        }
      },
      rowBuilder(row) {
        return [
          row.name ?? '',
          row.color ?? '',
          row.projectName ?? (row.projectId != null ? String(row.projectId) : '')
        ];
      },
      fields: [
        {
          key: 'projectId',
          label: 'Project',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/projects');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((p) => ({ value: p.id, label: p.name + (p.slug ? ` (${p.slug})` : '') }));
          }
        },
        { key: 'name', label: 'Name', type: 'text', required: true },
        { key: 'color', label: 'Color (hex or name)', type: 'text', required: true }
      ],
      toCreatePayload(v) {
        return {
          projectId: Number(v.projectId),
          name: v.name.trim(),
          color: v.color.trim()
        };
      },
      toUpdatePayload(v, id) {
        return {
          id: Number(id),
          projectId: Number(v.projectId),
          name: v.name.trim(),
          color: v.color.trim()
        };
      }
    },
    tasks: {
      apiPath: '/tasks',
      tableBodyId: 'tasksTableBody',
      title: 'Task',
      emptyCols: 7,
      canEdit: true,
      async enrichRows(list, API) {
        try {
          const [projectsData, sprintsData, taskTypesData, memberUsersData] = await Promise.all([
            API.request('/projects').catch(() => ({})),
            API.request('/sprints').catch(() => ({})),
            API.request('/task-types').catch(() => ({})),
            API.request('/member-user').catch(() => ({}))
          ]);
          const projects = Array.isArray(projectsData.dataList) ? projectsData.dataList : [];
          const sprints = Array.isArray(sprintsData.dataList) ? sprintsData.dataList : [];
          const taskTypes = Array.isArray(taskTypesData.dataList) ? taskTypesData.dataList : [];
          const memberUsers = Array.isArray(memberUsersData.dataList) ? memberUsersData.dataList : [];
          const projectMap = new Map(projects.map((p) => [p.id, p.name]));
          const sprintMap = new Map(sprints.map((s) => [s.id, s.name]));
          const taskTypeMap = new Map(taskTypes.map((t) => [t.id, t.name]));
          const userMap = new Map(memberUsers.map((u) => [u.id, u.name]));
          list.forEach((row) => {
            if (row.projectId != null) {
              row.projectName = projectMap.get(row.projectId) || `Project ${row.projectId}`;
            }
            if (row.sprintId != null) {
              row.sprintName = sprintMap.get(row.sprintId) || `Sprint ${row.sprintId}`;
            }
            if (row.taskTypeId != null) {
              row.taskTypeName = taskTypeMap.get(row.taskTypeId) || `Type ${row.taskTypeId}`;
            }
            if (row.assigneeId != null) {
              row.assigneeName = userMap.get(row.assigneeId) || `User ${row.assigneeId}`;
            }
          });
        } catch (e) {
          console.error('Failed to enrich rows', e);
        }
      },
      rowBuilder(row) {
        return [
          row.title ?? '',
          row.priority ?? '',
          row.projectName ?? (row.projectId != null ? String(row.projectId) : ''),
          row.sprintName ?? (row.sprintId != null ? String(row.sprintId) : ''),
          row.taskTypeName ?? (row.taskTypeId != null ? String(row.taskTypeId) : ''),
          row.dueDate != null ? String(row.dueDate) : ''
        ];
      },
      fields: [
        {
          key: 'projectId',
          label: 'Project',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/projects');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((p) => ({ value: p.id, label: p.name + (p.slug ? ` (${p.slug})` : '') }));
          }
        },
        {
          key: 'sprintId',
          label: 'Sprint',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/sprints');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((s) => ({ value: s.id, label: s.name }));
          }
        },
        {
          key: 'taskTypeId',
          label: 'Task Type',
          type: 'select',
          required: true,
          loadOptions: async () => {
            const data = await API.request('/task-types');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((t) => ({ value: t.id, label: `${t.icon || ''} ${t.name}`.trim() }));
          }
        },
        {
          key: 'parentTaskId',
          label: 'Parent task (optional)',
          type: 'select',
          optional: true,
          loadOptions: async () => {
            const data = await API.request('/tasks');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return [{ value: '', label: 'None' }].concat(
              list.map((t) => ({ value: t.id, label: t.title }))
            );
          }
        },
        { key: 'taskLevel', label: 'Task level', type: 'number', required: true, defaultValue: 1 },
        { key: 'rankOrder', label: 'Rank order', type: 'number', required: true, defaultValue: 1 },
        {
          key: 'assigneeId',
          label: 'Assignee (optional)',
          type: 'select',
          optional: true,
          loadOptions: async () => {
            const data = await API.request('/member-user');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return [{ value: '', label: 'Unassigned' }].concat(
              list.map((u) => ({ value: u.id, label: u.name }))
            );
          }
        },
        { key: 'title', label: 'Title', type: 'text', required: true },
        {
          key: 'priority',
          label: 'Priority',
          type: 'select',
          required: true,
          defaultValue: 'HIGH',
          options: [
            { value: 'HIGH', label: '🚩 High' },
            { value: 'MEDIUM', label: '⚠️ Medium' },
            { value: 'LOW', label: '🔵 Low' }
          ]
        },
        { key: 'status', label: 'Status / Stage', type: 'text', optional: true, defaultValue: 'OPEN' },
        { key: 'dueDate', label: 'Due date', type: 'date', required: true },
        {
          key: 'labelIds',
          label: 'Labels',
          type: 'multiselect',
          optional: true,
          loadOptions: async () => {
            const data = await API.request('/labels');
            const list = Array.isArray(data.dataList) ? data.dataList : [];
            return list.map((l) => ({ value: l.id, label: l.name }));
          }
        },
        { key: 'createAnother', label: 'Create another task (keep modal open)', type: 'checkbox' },
        { key: 'notifyAssignee', label: 'Notify assignee', type: 'checkbox', defaultValue: true }
      ],
      onFormBuilt(mode, row, API) {
        if (mode !== 'create') return;
        const projEl = document.getElementById('crud-field-projectId');
        const sprintEl = document.getElementById('crud-field-sprintId');
        const typeEl = document.getElementById('crud-field-taskTypeId');
        const assigneeEl = document.getElementById('crud-field-assigneeId');
        const rankEl = document.getElementById('crud-field-rankOrder');
        const statusEl = document.getElementById('crud-field-status');

        if (rankEl) {
          rankEl.value = '1';
          rankEl.readOnly = true;
        }

        if (statusEl) {
          statusEl.readOnly = true;
        }

        if (!projEl || !sprintEl || !typeEl) return;

        async function updateSprintAndType(projectId) {
          sprintEl.replaceChildren();
          typeEl.replaceChildren();
          if (assigneeEl) {
            assigneeEl.replaceChildren();
            const defOpt = document.createElement('option');
            defOpt.value = '';
            defOpt.textContent = 'Unassigned';
            assigneeEl.appendChild(defOpt);
          }
          if (!projectId) return;

          try {
            const [sRes, tRes, uRes] = await Promise.all([
              API.request(`/sprints/project/${projectId}`).catch(() => ({})),
              API.request(`/task-types/project/${projectId}`).catch(() => ({})),
              API.request(`/member-user/organization`).catch(() => ({}))
            ]);
            const sList = Array.isArray(sRes.dataList) ? sRes.dataList : [];
            const tList = Array.isArray(tRes.dataList) ? tRes.dataList : [];
            const uList = Array.isArray(uRes.dataList) ? uRes.dataList : [];

            sList.forEach((s) => {
              const opt = document.createElement('option');
              opt.value = s.id;
              opt.textContent = s.name;
              sprintEl.appendChild(opt);
            });

            tList.forEach((t) => {
              const opt = document.createElement('option');
              opt.value = t.id;
              opt.textContent = `${t.icon || ''} ${t.name}`.trim();
              if (t.workflowId != null) {
                opt.dataset.workflowId = t.workflowId;
              }
              typeEl.appendChild(opt);
            });

            if (assigneeEl) {
              uList.forEach((u) => {
                const opt = document.createElement('option');
                opt.value = u.id;
                opt.textContent = u.name;
                assigneeEl.appendChild(opt);
              });
            }

            typeEl.dispatchEvent(new Event('change'));
          } catch (e) { console.error(e); }
        }

        projEl.addEventListener('change', () => updateSprintAndType(projEl.value));

        typeEl.addEventListener('change', async () => {
          const selOpt = typeEl.selectedOptions[0];
          if (!selOpt || !selOpt.dataset.workflowId) {
            if (statusEl) statusEl.value = 'OPEN';
            return;
          }
          try {
            const res = await API.request('/workflow-states');
            const states = Array.isArray(res.dataList) ? res.dataList : [];
            const wfId = parseInt(selOpt.dataset.workflowId, 10);
            const initialSt = states.find((s) => s.workflowId === wfId && s.isInitial);
            if (initialSt && statusEl) {
              statusEl.value = initialSt.name;
            } else if (statusEl) {
              statusEl.value = 'OPEN';
            }
          } catch (e) { console.error(e); }
        });

        if (projEl.value) {
          updateSprintAndType(projEl.value);
        }
      },
      toCreatePayload(v) {
        return {
          projectId: Number(v.projectId),
          sprintId: Number(v.sprintId),
          taskTypeId: Number(v.taskTypeId),
          parentTaskId: v.parentTaskId && String(v.parentTaskId).trim() !== '' ? Number(v.parentTaskId) : null,
          taskLevel: Number(v.taskLevel),
          rankOrder: Number(v.rankOrder),
          assigneeId: v.assigneeId && String(v.assigneeId).trim() !== '' ? Number(v.assigneeId) : null,
          title: v.title.trim(),
          priority: v.priority.trim(),
          status: v.status ? v.status.trim() : 'OPEN',
          dueDate: v.dueDate || null,
          labelIds: Array.isArray(v.labelIds) ? v.labelIds.map(Number) : []
        };
      },
      toUpdatePayload(v, id) {
        return {
          id: Number(id),
          projectId: Number(v.projectId),
          sprintId: Number(v.sprintId),
          taskTypeId: Number(v.taskTypeId),
          parentTaskId: v.parentTaskId && String(v.parentTaskId).trim() !== '' ? Number(v.parentTaskId) : null,
          taskLevel: Number(v.taskLevel),
          rankOrder: Number(v.rankOrder),
          assigneeId: v.assigneeId && String(v.assigneeId).trim() !== '' ? Number(v.assigneeId) : null,
          title: v.title.trim(),
          priority: v.priority.trim(),
          status: v.status ? v.status.trim() : 'OPEN',
          dueDate: v.dueDate || null,
          labelIds: Array.isArray(v.labelIds) ? v.labelIds.map(Number) : []
        };
      }
    }
  };

  let crudState = { view: null, mode: null, fields: [] };

  function resolveCrudFields(cfg, mode) {
    if (mode === 'edit' && cfg.editFields) return cfg.editFields;
    if (mode === 'create') return cfg.fields.filter((f) => !f.editOnly);
    return cfg.fields.filter((f) => !f.createOnly);
  }

  function buildCrudForm(cfg, mode, row) {
    const body = document.getElementById('dynamicCrudBody');
    body.replaceChildren();
    const fields = resolveCrudFields(cfg, mode);
    crudState.fields = fields;
    fields.forEach((f) => {
      const wrap = document.createElement('div');
      wrap.className = 'form-group';
      const label = document.createElement('label');
      label.htmlFor = `crud-field-${f.key}`;
      label.textContent = f.label;
      wrap.appendChild(label);
      let input;
      if (f.type === 'checkbox') {
        input = document.createElement('input');
        input.type = 'checkbox';
        input.id = `crud-field-${f.key}`;
        const checked = row && row[f.key] != null ? Boolean(row[f.key]) : false;
        input.checked = checked;
      } else if (f.type === 'multiselect') {
        input = document.createElement('select');
        input.multiple = true;
        input.id = `crud-field-${f.key}`;
        input.style.height = '160px';
        if (f.loadOptions) {
          f.loadOptions().then((opts) => {
            const currentVals = row && Array.isArray(row[f.key]) ? row[f.key] : [];
            opts.forEach((opt) => {
              const optionEl = document.createElement('option');
              optionEl.value = opt.value;
              optionEl.textContent = opt.label;
              if (currentVals.includes(opt.value)) {
                optionEl.selected = true;
              }
              input.appendChild(optionEl);
            });
          }).catch((err) => console.error('Failed to load options', err));
        }
      } else if (f.type === 'select') {
        input = document.createElement('select');
        input.id = `crud-field-${f.key}`;
        let currentVal = '';
        if (row) {
          if (f.fromRow) currentVal = f.fromRow(row);
          else if (row[f.key] != null) currentVal = String(row[f.key]);
        } else if (f.defaultValue != null) {
          currentVal = String(f.defaultValue);
        }

        if (f.options) {
          f.options.forEach((opt) => {
            const optionEl = document.createElement('option');
            optionEl.value = opt.value;
            optionEl.textContent = opt.label;
            if (String(opt.value) === String(currentVal)) {
              optionEl.selected = true;
            }
            input.appendChild(optionEl);
          });
        } else if (f.loadOptions) {
          const promptOpt = document.createElement('option');
          promptOpt.value = '';
          promptOpt.textContent = f.placeholder || 'Select...';
          input.appendChild(promptOpt);

          f.loadOptions().then((opts) => {
            opts.forEach((opt) => {
              const optionEl = document.createElement('option');
              optionEl.value = opt.value;
              optionEl.textContent = opt.label;
              if (String(opt.value) === String(currentVal)) {
                optionEl.selected = true;
              }
              input.appendChild(optionEl);
            });
          }).catch((err) => console.error('Failed to load options', err));
        }
      } else {
        input = document.createElement('input');
        input.type = f.type === 'number' ? 'number' : f.type;
        input.id = `crud-field-${f.key}`;
        if (f.placeholder) input.placeholder = f.placeholder;
        let val = '';
        if (row) {
          if (f.fromRow) val = f.fromRow(row);
          else if (row[f.key] != null) val = String(row[f.key]);
        }
        input.value = val;
      }
      wrap.appendChild(input);
      body.appendChild(wrap);
    });
    if (cfg.onFormBuilt) {
      cfg.onFormBuilt(mode, row, API);
    }
  }

  function collectCrudValues() {
    const vals = {};
    crudState.fields.forEach((f) => {
      const el = document.getElementById(`crud-field-${f.key}`);
      if (!el) return;
      if (f.type === 'checkbox') {
        vals[f.key] = el.checked;
        return;
      }
      if (f.type === 'multiselect') {
        const selectedOptions = Array.from(el.selectedOptions).map((o) => Number(o.value));
        vals[f.key] = selectedOptions;
        return;
      }
      const raw = el.value;
      if (f.parse) {
        vals[f.key] =
          f.optional && String(raw).trim() === '' ? (f.key === 'labelIds' ? [] : null) : f.parse(raw);
        return;
      }
      if (f.type === 'number') {
        vals[f.key] = f.optional && String(raw).trim() === '' ? null : Number(raw);
        return;
      }
      vals[f.key] = raw;
    });
    return vals;
  }

  let workflowWizard = {
    step: 1,
    mode: 'create',
    workflowId: null,
    myStates: [],
    myTransitions: [],
    row: null
  };

  function renderWorkflowWizardStep() {
    const heading = document.getElementById('dynamicCrudHeading');
    const body = document.getElementById('dynamicCrudBody');
    const saveBtn = document.getElementById('dynamicCrudSave');
    body.replaceChildren();

    if (workflowWizard.mode === 'create') {
      if (workflowWizard.step === 1) {
        heading.textContent = 'Add Workflow (Step 1 of 3: Create States)';
        saveBtn.textContent = 'Next →';
        const listDiv = document.createElement('div');
        listDiv.style.marginBottom = '16px';
        listDiv.innerHTML = '<h4>Created States:</h4>';
        if (workflowWizard.myStates.length === 0) {
          listDiv.innerHTML += '<p style="color:#64748b;font-size:14px;margin-top:4px;">No states added yet.</p>';
        } else {
          const ul = document.createElement('ul');
          ul.style.paddingLeft = '20px';
          ul.style.marginTop = '6px';
          workflowWizard.myStates.forEach((s) => {
            const li = document.createElement('li');
            li.textContent = `${s.name} (Seq: ${s.sequenceNo}) [Initial: ${Boolean(s.isInitial)}, Final: ${Boolean(s.isFinal)}]`;
            li.style.marginBottom = '4px';
            ul.appendChild(li);
          });
          listDiv.appendChild(ul);
        }
        body.appendChild(listDiv);

        const formDiv = document.createElement('div');
        formDiv.style.background = '#f1f5f9';
        formDiv.style.padding = '14px';
        formDiv.style.borderRadius = '8px';
        formDiv.innerHTML = `
          <h5 style="margin-bottom:12px;font-size:15px;">Add New State</h5>
          <div class="form-group"><label style="display:block;margin-bottom:4px;font-size:13px;font-weight:600;">Name</label><input type="text" id="wizStateName" style="width:100%;padding:8px;border:1px solid #cbd5e1;border-radius:4px;" /></div>
          <div class="form-group"><label style="display:block;margin-bottom:4px;font-size:13px;font-weight:600;">Sequence No</label><input type="number" id="wizStateSeq" style="width:100%;padding:8px;border:1px solid #cbd5e1;border-radius:4px;" /></div>
          <div class="form-group" style="display:flex;align-items:center;gap:8px;margin-bottom:8px;"><input type="checkbox" id="wizStateInit" /><label style="font-size:13px;font-weight:600;">Is Initial</label></div>
          <div class="form-group" style="display:flex;align-items:center;gap:8px;margin-bottom:14px;"><input type="checkbox" id="wizStateFinal" /><label style="font-size:13px;font-weight:600;">Is Final</label></div>
          <button type="button" class="secondary-btn" id="wizAddStateBtn">+ Add State</button>
        `;
        body.appendChild(formDiv);

        document.getElementById('wizAddStateBtn').addEventListener('click', async () => {
          const name = document.getElementById('wizStateName').value.trim();
          const seq = parseInt(document.getElementById('wizStateSeq').value, 10);
          const init = document.getElementById('wizStateInit').checked;
          const fin = document.getElementById('wizStateFinal').checked;
          if (!name || Number.isNaN(seq)) {
            window.showToast('Name and Sequence No are required');
            return;
          }
          try {
            const res = await API.request('/workflow-states', 'POST', { name, sequenceNo: seq, isInitial: init, isFinal: fin });
            if (res && res.dataList && res.dataList[0]) {
              workflowWizard.myStates.push(res.dataList[0]);
              window.showToast('State added');
              renderWorkflowWizardStep();
            }
          } catch (e) { window.showToast('Add state failed'); }
        });
      } else if (workflowWizard.step === 2) {
        heading.textContent = 'Add Workflow (Step 2 of 3: Create Transitions)';
        saveBtn.textContent = 'Next →';
        const listDiv = document.createElement('div');
        listDiv.style.marginBottom = '16px';
        listDiv.innerHTML = '<h4>Created Transitions:</h4>';
        const stateMap = new Map(workflowWizard.myStates.map((s) => [s.id, s.name]));
        if (workflowWizard.myTransitions.length === 0) {
          listDiv.innerHTML += '<p style="color:#64748b;font-size:14px;margin-top:4px;">No transitions added yet.</p>';
        } else {
          const ul = document.createElement('ul');
          ul.style.paddingLeft = '20px';
          ul.style.marginTop = '6px';
          workflowWizard.myTransitions.forEach((t) => {
            const li = document.createElement('li');
            li.textContent = `${stateMap.get(t.fromStateId)} → ${stateMap.get(t.toStateId)}`;
            li.style.marginBottom = '4px';
            ul.appendChild(li);
          });
          listDiv.appendChild(ul);
        }
        body.appendChild(listDiv);

        let optionsHtml = workflowWizard.myStates.map((s) => `<option value="${s.id}">${s.name}</option>`).join('');
        const formDiv = document.createElement('div');
        formDiv.style.background = '#f1f5f9';
        formDiv.style.padding = '14px';
        formDiv.style.borderRadius = '8px';
        formDiv.innerHTML = `
          <h5 style="margin-bottom:12px;font-size:15px;">Add New Transition</h5>
          <div class="form-group"><label style="display:block;margin-bottom:4px;font-size:13px;font-weight:600;">From State</label><select id="wizTransFrom" style="width:100%;padding:8px;border:1px solid #cbd5e1;border-radius:4px;background:white;">${optionsHtml}</select></div>
          <div class="form-group"><label style="display:block;margin-bottom:4px;font-size:13px;font-weight:600;">To State</label><select id="wizTransTo" style="width:100%;padding:8px;border:1px solid #cbd5e1;border-radius:4px;background:white;">${optionsHtml}</select></div>
          <button type="button" class="secondary-btn" id="wizAddTransBtn">+ Add Transition</button>
        `;
        body.appendChild(formDiv);

        document.getElementById('wizAddTransBtn').addEventListener('click', async () => {
          const fromId = parseInt(document.getElementById('wizTransFrom').value, 10);
          const toId = parseInt(document.getElementById('wizTransTo').value, 10);
          if (Number.isNaN(fromId) || Number.isNaN(toId)) {
            window.showToast('Please select states');
            return;
          }
          try {
            const res = await API.request('/workflow-transitions', 'POST', { fromStateId: fromId, toStateId: toId });
            if (res && res.dataList && res.dataList[0]) {
              workflowWizard.myTransitions.push(res.dataList[0]);
              window.showToast('Transition added');
              renderWorkflowWizardStep();
            }
          } catch (e) { window.showToast('Add transition failed'); }
        });
      } else if (workflowWizard.step === 3) {
        heading.textContent = 'Add Workflow (Step 3 of 3: Workflow Details)';
        saveBtn.textContent = 'Save Workflow';
        buildCrudForm(ENTITY_CONFIG['workflows'], 'create', null);
      }
    } else {
      if (workflowWizard.step === 1) {
        heading.textContent = 'Edit Workflow (Step 1 of 3: Edit Transitions)';
        saveBtn.textContent = 'Skip / Next →';
        const listDiv = document.createElement('div');
        listDiv.style.marginBottom = '16px';
        listDiv.innerHTML = '<h4>Existing Transitions (Can delete/add):</h4>';
        const stateMap = new Map(workflowWizard.myStates.map((s) => [s.id, s.name]));
        if (workflowWizard.myTransitions.length === 0) {
          listDiv.innerHTML += '<p style="color:#64748b;font-size:14px;margin-top:4px;">No transitions found.</p>';
        } else {
          const wrapDiv = document.createElement('div');
          wrapDiv.style.marginTop = '8px';
          workflowWizard.myTransitions.forEach((t) => {
            const rowDiv = document.createElement('div');
            rowDiv.style.display = 'flex';
            rowDiv.style.alignItems = 'center';
            rowDiv.style.justifyContent = 'space-between';
            rowDiv.style.padding = '8px';
            rowDiv.style.background = '#f8fafc';
            rowDiv.style.border = '1px solid #e2e8f0';
            rowDiv.style.borderRadius = '6px';
            rowDiv.style.marginBottom = '6px';
            rowDiv.innerHTML = `<span style="font-size:14px;font-weight:500;">${stateMap.get(t.fromStateId) || `State ${t.fromStateId}`} → ${stateMap.get(t.toStateId) || `State ${t.toStateId}`}</span>`;
            const delBtn = document.createElement('button');
            delBtn.type = 'button';
            delBtn.className = 'secondary-btn';
            delBtn.textContent = 'Delete';
            delBtn.addEventListener('click', async () => {
              if (!confirm('Delete transition?')) return;
              try {
                await API.request(`/workflow-transitions/${t.id}`, 'DELETE');
                workflowWizard.myTransitions = workflowWizard.myTransitions.filter((x) => x.id !== t.id);
                window.showToast('Deleted transition');
                renderWorkflowWizardStep();
              } catch (e) { window.showToast('Delete failed'); }
            });
            rowDiv.appendChild(delBtn);
            wrapDiv.appendChild(rowDiv);
          });
          listDiv.appendChild(wrapDiv);
        }
        body.appendChild(listDiv);

        let optionsHtml = workflowWizard.myStates.map((s) => `<option value="${s.id}">${s.name}</option>`).join('');
        const formDiv = document.createElement('div');
        formDiv.style.background = '#f1f5f9';
        formDiv.style.padding = '14px';
        formDiv.style.borderRadius = '8px';
        formDiv.innerHTML = `
          <h5 style="margin-bottom:12px;font-size:15px;">Add New Transition</h5>
          <div class="form-group"><label style="display:block;margin-bottom:4px;font-size:13px;font-weight:600;">From State</label><select id="wizTransFrom" style="width:100%;padding:8px;border:1px solid #cbd5e1;border-radius:4px;background:white;">${optionsHtml}</select></div>
          <div class="form-group"><label style="display:block;margin-bottom:4px;font-size:13px;font-weight:600;">To State</label><select id="wizTransTo" style="width:100%;padding:8px;border:1px solid #cbd5e1;border-radius:4px;background:white;">${optionsHtml}</select></div>
          <button type="button" class="secondary-btn" id="wizAddTransBtn">+ Add Transition</button>
        `;
        body.appendChild(formDiv);

        document.getElementById('wizAddTransBtn').addEventListener('click', async () => {
          const fromId = parseInt(document.getElementById('wizTransFrom').value, 10);
          const toId = parseInt(document.getElementById('wizTransTo').value, 10);
          if (Number.isNaN(fromId) || Number.isNaN(toId)) return;
          try {
            const res = await API.request('/workflow-transitions', 'POST', { workflowId: workflowWizard.workflowId, fromStateId: fromId, toStateId: toId });
            if (res && res.dataList && res.dataList[0]) {
              workflowWizard.myTransitions.push(res.dataList[0]);
              window.showToast('Transition added');
              renderWorkflowWizardStep();
            }
          } catch (e) { window.showToast('Add transition failed'); }
        });
      } else if (workflowWizard.step === 2) {
        heading.textContent = 'Edit Workflow (Step 2 of 3: Edit States)';
        saveBtn.textContent = 'Next →';
        const listDiv = document.createElement('div');
        listDiv.style.marginBottom = '16px';
        listDiv.innerHTML = '<h4>Existing States:</h4>';
        if (workflowWizard.myStates.length === 0) {
          listDiv.innerHTML += '<p style="color:#64748b;font-size:14px;margin-top:4px;">No states found.</p>';
        } else {
          const wrapDiv = document.createElement('div');
          wrapDiv.style.marginTop = '8px';
          workflowWizard.myStates.forEach((s) => {
            const rowDiv = document.createElement('div');
            rowDiv.style.display = 'flex';
            rowDiv.style.alignItems = 'center';
            rowDiv.style.justifyContent = 'space-between';
            rowDiv.style.padding = '8px';
            rowDiv.style.background = '#f8fafc';
            rowDiv.style.border = '1px solid #e2e8f0';
            rowDiv.style.borderRadius = '6px';
            rowDiv.style.marginBottom = '6px';
            rowDiv.innerHTML = `<span style="font-size:14px;font-weight:500;">${s.name} (Seq: ${s.sequenceNo})</span>`;
            const delBtn = document.createElement('button');
            delBtn.type = 'button';
            delBtn.className = 'secondary-btn';
            delBtn.textContent = 'Delete';
            delBtn.addEventListener('click', async () => {
              if (!confirm('Delete state?')) return;
              try {
                await API.request(`/workflow-states/${s.id}`, 'DELETE');
                workflowWizard.myStates = workflowWizard.myStates.filter((x) => x.id !== s.id);
                window.showToast('Deleted state');
                renderWorkflowWizardStep();
              } catch (e) { window.showToast('Delete failed'); }
            });
            rowDiv.appendChild(delBtn);
            wrapDiv.appendChild(rowDiv);
          });
          listDiv.appendChild(wrapDiv);
        }
        body.appendChild(listDiv);

        const formDiv = document.createElement('div');
        formDiv.style.background = '#f1f5f9';
        formDiv.style.padding = '14px';
        formDiv.style.borderRadius = '8px';
        formDiv.innerHTML = `
          <h5 style="margin-bottom:12px;font-size:15px;">Add New State</h5>
          <div class="form-group"><label style="display:block;margin-bottom:4px;font-size:13px;font-weight:600;">Name</label><input type="text" id="wizStateName" style="width:100%;padding:8px;border:1px solid #cbd5e1;border-radius:4px;" /></div>
          <div class="form-group"><label style="display:block;margin-bottom:4px;font-size:13px;font-weight:600;">Sequence No</label><input type="number" id="wizStateSeq" style="width:100%;padding:8px;border:1px solid #cbd5e1;border-radius:4px;" /></div>
          <div class="form-group" style="display:flex;align-items:center;gap:8px;margin-bottom:8px;"><input type="checkbox" id="wizStateInit" /><label style="font-size:13px;font-weight:600;">Is Initial</label></div>
          <div class="form-group" style="display:flex;align-items:center;gap:8px;margin-bottom:14px;"><input type="checkbox" id="wizStateFinal" /><label style="font-size:13px;font-weight:600;">Is Final</label></div>
          <button type="button" class="secondary-btn" id="wizAddStateBtn">+ Add State</button>
        `;
        body.appendChild(formDiv);

        document.getElementById('wizAddStateBtn').addEventListener('click', async () => {
          const name = document.getElementById('wizStateName').value.trim();
          const seq = parseInt(document.getElementById('wizStateSeq').value, 10);
          const init = document.getElementById('wizStateInit').checked;
          const fin = document.getElementById('wizStateFinal').checked;
          if (!name || Number.isNaN(seq)) return;
          try {
            const res = await API.request('/workflow-states', 'POST', { workflowId: workflowWizard.workflowId, name, sequenceNo: seq, isInitial: init, isFinal: fin });
            if (res && res.dataList && res.dataList[0]) {
              workflowWizard.myStates.push(res.dataList[0]);
              window.showToast('State added');
              renderWorkflowWizardStep();
            }
          } catch (e) { window.showToast('Add state failed'); }
        });
      } else if (workflowWizard.step === 3) {
        heading.textContent = 'Edit Workflow (Step 3 of 3: Workflow Details)';
        saveBtn.textContent = 'Save Workflow';
        buildCrudForm(ENTITY_CONFIG['workflows'], 'edit', workflowWizard.row);
      }
    }
  }

  async function openEntityModal(view, mode, row) {
    const cfg = ENTITY_CONFIG[view];
    if (!cfg) return;

    if (view === 'workflows') {
      crudState = { view: 'workflows', mode, fields: [] };
      document.getElementById('crudRecordId').value = mode === 'edit' && row && row.id != null ? String(row.id) : '';
      workflowWizard.mode = mode;
      workflowWizard.row = row;
      workflowWizard.workflowId = row ? row.id : null;
      workflowWizard.myStates = [];
      workflowWizard.myTransitions = [];
      workflowWizard.step = 1;

      if (mode === 'edit') {
        try {
          const [statesRes, transRes] = await Promise.all([
            API.request('/workflow-states').catch(() => ({})),
            API.request('/workflow-transitions').catch(() => ({}))
          ]);
          const sList = Array.isArray(statesRes.dataList) ? statesRes.dataList : [];
          const tList = Array.isArray(transRes.dataList) ? transRes.dataList : [];
          workflowWizard.myStates = sList.filter((s) => s.workflowId === workflowWizard.workflowId);
          workflowWizard.myTransitions = tList.filter((t) => t.workflowId === workflowWizard.workflowId);
        } catch (e) { console.error(e); }
      }

      renderWorkflowWizardStep();
      document.getElementById('dynamicCrudModal').classList.add('active');
      return;
    }

    crudState = { view, mode, fields: [] };
    document.getElementById('crudRecordId').value =
      mode === 'edit' && row && row.id != null ? String(row.id) : '';
    document.getElementById('dynamicCrudHeading').textContent =
      mode === 'edit' ? `Edit ${cfg.title}` : `Add ${cfg.title}`;
    buildCrudForm(cfg, mode, row || null);
    document.getElementById('dynamicCrudModal').classList.add('active');
  }

  function closeDynamicCrud() {
    document.getElementById('dynamicCrudModal').classList.remove('active');
    document.getElementById('dynamicCrudSave').textContent = 'Save';
    crudState = { view: null, mode: null, fields: [] };
  }

  async function renderKanbanBoard() {
    const kanbanCont = document.getElementById('kanbanBoardContainer');
    if (!kanbanCont) return;
    kanbanCont.replaceChildren();

    try {
      const res = await API.request('/task-dashboard').catch(() => ({}));
      const rawKeys = res.dataMap && res.dataMap.keysToShow ? res.dataMap.keysToShow : [];
      const keys = Array.isArray(rawKeys) && rawKeys.length > 0 ? rawKeys : ['OPEN', 'IN PROGRESS', 'DONE'];
      const tasks = Array.isArray(res.dataList) ? res.dataList : [];

      if (ENTITY_CONFIG.tasks.enrichRows) {
        await ENTITY_CONFIG.tasks.enrichRows(tasks, API);
      }

      keys.forEach((key) => {
        const col = document.createElement('div');
        col.className = 'kanban-column';
        col.style.flex = '1';
        col.style.minWidth = '300px';
        col.style.background = '#f8fafc';
        col.style.borderRadius = '12px';
        col.style.padding = '16px';
        col.style.border = '1px solid #e2e8f0';

        const matchingTasks = tasks.filter((t) => {
          const s = (t.status || 'OPEN').trim();
          return s.toUpperCase() === key.toUpperCase();
        });

        let colColor = '#3b82f6';
        if (key.toUpperCase().includes('PROG')) colColor = '#d97706';
        if (key.toUpperCase().includes('DONE') || key.toUpperCase().includes('COMP')) colColor = '#16a34a';

        col.innerHTML = `
          <h3 style="font-size: 15px; font-weight: 600; margin-bottom: 12px; display: flex; justify-content: space-between; color: ${colColor};">
            <span>🔵 ${key.toUpperCase()}</span> <span class="count">${matchingTasks.length}</span>
          </h3>
          <div class="kanban-cards" style="display: flex; flex-direction: column; gap: 12px; min-height: 200px;"></div>
        `;

        const cardsCont = col.querySelector('.kanban-cards');

        col.addEventListener('dragover', (e) => {
          e.preventDefault();
          col.style.borderColor = '#3b82f6';
          col.style.background = '#f1f5f9';
        });

        col.addEventListener('dragleave', () => {
          col.style.borderColor = '#e2e8f0';
          col.style.background = '#f8fafc';
        });

        col.addEventListener('drop', async (e) => {
          e.preventDefault();
          col.style.borderColor = '#e2e8f0';
          col.style.background = '#f8fafc';
          const taskIdStr = e.dataTransfer.getData('text/plain');
          const taskId = parseInt(taskIdStr, 10);
          if (Number.isNaN(taskId)) return;

          const task = tasks.find((t) => t.id === taskId);
          if (!task) return;

          try {
            await API.request('/tasks', 'PUT', {
              id: task.id,
              projectId: task.projectId,
              sprintId: task.sprintId,
              taskTypeId: task.taskTypeId,
              parentTaskId: task.parentTaskId,
              taskLevel: task.taskLevel != null ? task.taskLevel : 1,
              rankOrder: task.rankOrder != null ? task.rankOrder : 1,
              assigneeId: task.assigneeId,
              title: task.title,
              priority: task.priority || 'MEDIUM',
              dueDate: task.dueDate,
              labelIds: task.labelIds,
              status: key
            });
            window.showToast(`Moved task to ${key}`);
            renderKanbanBoard();
          } catch (err) {
            console.error(err);
            window.showToast('Failed to move task');
          }
        });

        matchingTasks.forEach((t) => {
          const card = document.createElement('div');
          card.draggable = true;
          card.style.background = 'white';
          card.style.padding = '16px';
          card.style.borderRadius = '8px';
          card.style.boxShadow = '0 1px 3px rgba(0,0,0,0.1)';
          card.style.border = '1px solid #cbd5e1';
          card.style.cursor = 'grab';

          card.addEventListener('dragstart', (e) => {
            e.dataTransfer.setData('text/plain', t.id);
          });

          let prioColor = '#3b82f6';
          if (t.priority === 'HIGH') prioColor = '#ef4444';
          if (t.priority === 'MEDIUM') prioColor = '#f59e0b';

          card.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
              <span style="font-size: 12px; font-weight: 600; color: #64748b; background: #f1f5f9; padding: 2px 6px; border-radius: 4px;">${t.taskTypeName || 'Task'}</span>
              <span style="font-size: 12px; font-weight: 600; color: ${prioColor};">🚩 ${t.priority || 'LOW'}</span>
            </div>
            <div style="font-size: 14px; font-weight: 600; color: #1e293b; margin-bottom: 8px;">${t.title}</div>
            <div style="font-size: 12px; color: #64748b; margin-bottom: 12px;">
              <div>📅 Due: ${t.dueDate || 'No date'}</div>
              <div>👤 Assignee: ${t.assigneeName || 'Unassigned'}</div>
            </div>
          `;

          const editBtn = document.createElement('button');
          editBtn.type = 'button';
          editBtn.className = 'secondary-btn';
          editBtn.style.fontSize = '11px';
          editBtn.style.padding = '4px 8px';
          editBtn.textContent = 'Edit';
          editBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            openEntityModal('tasks', 'edit', t);
          });

          const cardActions = document.createElement('div');
          cardActions.style.display = 'flex';
          cardActions.style.justifyContent = 'flex-end';
          cardActions.appendChild(editBtn);
          card.appendChild(cardActions);

          cardsCont.appendChild(card);
        });

        kanbanCont.appendChild(col);
      });

    } catch (e) { console.error('Kanban load error', e); }
  }

  async function saveDynamicCrud() {
    const { view, mode } = crudState;
    if (view === 'workflows') {
      if (workflowWizard.step < 3) {
        workflowWizard.step += 1;
        renderWorkflowWizardStep();
        return;
      }
      const cfg = ENTITY_CONFIG[view];
      const vals = collectCrudValues();
      for (const f of crudState.fields) {
        if (f.type === 'checkbox') continue;
        if (f.required) {
          const v = vals[f.key];
          if (v === '' || v == null || (typeof v === 'number' && Number.isNaN(v)) || (Array.isArray(v) && v.length === 0)) {
            window.showToast(`${f.label} is required`);
            return;
          }
        }
      }
      const idStr = document.getElementById('crudRecordId').value;
      try {
        if (mode === 'edit') {
          await API.request(cfg.apiPath, 'PUT', cfg.toUpdatePayload(vals, idStr));
          window.showToast('Updated workflow');
        } else {
          const payload = cfg.toCreatePayload(vals);
          payload.stateIds = workflowWizard.myStates.map((s) => s.id);
          payload.transitionIds = workflowWizard.myTransitions.map((t) => t.id);
          await API.request(cfg.apiPath, 'POST', payload);
          window.showToast('Created workflow');
        }
        closeDynamicCrud();
        await loadTable(view);
        await window.refreshDashboardStats();
      } catch (e) {
        console.error(e);
        window.showToast('Save workflow failed');
      }
      return;
    }

    const cfg = ENTITY_CONFIG[view];
    if (!cfg) return;
    const vals = collectCrudValues();
    for (const f of crudState.fields) {
      if (f.type === 'checkbox') continue;
      if (f.required) {
        const v = vals[f.key];
        if (v === '' || v == null || (typeof v === 'number' && Number.isNaN(v)) || (Array.isArray(v) && v.length === 0)) {
          window.showToast(`${f.label} is required`);
          return;
        }
      }
    }
    const idStr = document.getElementById('crudRecordId').value;
    try {
      if (mode === 'edit') {
        if (!cfg.toUpdatePayload) {
          window.showToast('Update not supported');
          return;
        }
        await API.request(cfg.apiPath, 'PUT', cfg.toUpdatePayload(vals, idStr));
        window.showToast('Updated');
      } else {
        await API.request(cfg.apiPath, 'POST', cfg.toCreatePayload(vals));
        window.showToast('Created');
      }

      if (view === 'tasks' && vals.createAnother) {
        window.showToast(mode === 'edit' ? 'Updated task' : 'Created task');
        await loadTable(view);
        await renderKanbanBoard();
        await window.refreshDashboardStats();
        openEntityModal('tasks', 'create', null);
        return;
      }

      closeDynamicCrud();
      await loadTable(view);
      await renderKanbanBoard();
      await window.refreshDashboardStats();
    } catch (e) {
      console.error(e);
      window.showToast('Save failed');
    }
  }

  async function deleteEntity(view, id) {
    const cfg = ENTITY_CONFIG[view];
    if (!cfg || id == null) return;
    if (!confirm('Delete this record?')) return;
    try {
      await API.request(`${cfg.apiPath}/${id}`, 'DELETE');
      window.showToast('Deleted');
      await loadTable(view);
      await renderKanbanBoard();
      await window.refreshDashboardStats();
    } catch (e) {
      console.error(e);
      window.showToast('Delete failed');
    }
  }

  async function loadTable(view) {
    const cfg = ENTITY_CONFIG[view];
    if (!cfg) return;
    const tbody = document.getElementById(cfg.tableBodyId);
    if (!tbody) return;
    const cols = cfg.emptyCols;
    tbody.replaceChildren();
    const loadingTr = document.createElement('tr');
    const loadingTd = document.createElement('td');
    loadingTd.colSpan = cols;
    loadingTd.textContent = 'Loading…';
    loadingTr.appendChild(loadingTd);
    tbody.appendChild(loadingTr);
    try {
      const data = await API.request(cfg.apiPath);
      const list = Array.isArray(data.dataList) ? data.dataList : [];
      if (cfg.enrichRows) {
        await cfg.enrichRows(list, API);
      }
      tbody.replaceChildren();
      if (list.length === 0) {
        const tr = document.createElement('tr');
        const td = document.createElement('td');
        td.colSpan = cols;
        td.textContent = 'No records yet.';
        tr.appendChild(td);
        tbody.appendChild(tr);
        return;
      }
      list.forEach((row) => {
        const tr = document.createElement('tr');
        const cells = cfg.rowBuilder(row);
        cells.forEach((text) => {
          const td = document.createElement('td');
          td.textContent = text;
          tr.appendChild(td);
        });
        if (cfg.canEdit !== false || cfg.canDelete !== false) {
          const actionsTd = document.createElement('td');
          if (cfg.canEdit !== false) {
            const editBtn = document.createElement('button');
            editBtn.type = 'button';
            editBtn.className = 'secondary-btn';
            editBtn.textContent = 'Edit';
            editBtn.addEventListener('click', () => openEntityModal(view, 'edit', row));
            actionsTd.appendChild(editBtn);
          }
          if (cfg.canDelete !== false) {
            const delBtn = document.createElement('button');
            delBtn.type = 'button';
            delBtn.className = 'secondary-btn';
            delBtn.style.marginLeft = '8px';
            delBtn.textContent = 'Delete';
            delBtn.addEventListener('click', () => deleteEntity(view, row.id));
            actionsTd.appendChild(delBtn);
          }
          tr.appendChild(actionsTd);
        }
        tbody.appendChild(tr);
      });
    } catch (error) {
      if (error.message === '401') return;
      tbody.replaceChildren();
      const tr = document.createElement('tr');
      const td = document.createElement('td');
      td.colSpan = cols;
      td.textContent = `Could not load (${error.message})`;
      tr.appendChild(td);
      tbody.appendChild(tr);
    }
  }

  function navigateTo(view) {
    document.querySelectorAll('.page-panel').forEach((el) => {
      el.classList.toggle('hidden', el.id !== `page-${view}`);
    });
    document.querySelectorAll('.menu-item[data-nav]').forEach((el) => {
      el.classList.toggle('active', el.getAttribute('data-nav') === view);
    });
    if (TABLE_VIEW_KEYS.includes(view)) {
      loadTable(view);
    }
    if (view === 'kanban') {
      renderKanbanBoard();
    }
  }

  window.navigateTo = navigateTo;
  window.loadEntityTable = loadTable;
  window.openEntityModal = openEntityModal;
  window.closeDynamicCrud = closeDynamicCrud;
  window.saveDynamicCrud = saveDynamicCrud;

  document.querySelectorAll('.crud-add').forEach((btn) => {
    btn.addEventListener('click', () => {
      const view = btn.getAttribute('data-view');
      if (view) openEntityModal(view, 'create', null);
    });
  });

  const dynModal = document.getElementById('dynamicCrudModal');
  document.getElementById('dynamicCrudCancel').addEventListener('click', () => closeDynamicCrud());
  document.getElementById('dynamicCrudSave').addEventListener('click', () => saveDynamicCrud());
  document.getElementById('dynamicCrudClose').addEventListener('click', () => closeDynamicCrud());
}

window.registerEntityCrud = registerEntityCrud;
