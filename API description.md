# VelocityFlow API Description

This document maps all REST endpoints in the VelocityFlow application to their corresponding `@RequirePermission` constants and provides a brief explanation of each endpoint's purpose.

## Admin Users
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Admin | `POST /admin-user` | `ADMIN_USER_CREATE` | Creates a new platform administrator. |
| Update Admin | `PUT /admin-user` | `ADMIN_USER_UPDATE` | Updates an existing admin's details. |
| Get Admin | `GET /admin-user/{id}` | `ADMIN_USER_READ` | Retrieves a specific admin by their ID. |
| Get All Admins | `GET /admin-user` | `ADMIN_USER_READ` | Retrieves a list of all administrators. |
| Delete Admin | `DELETE /admin-user/{id}` | `ADMIN_USER_DELETE` | Hard-deletes an admin from the system. |

## Member Users
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Member | `POST /member-user` | `MEMBER_USER_CREATE` | Creates a standard platform member. |
| Update Member | `PUT /member-user` | `MEMBER_USER_UPDATE` | Updates a standard member's profile. |
| Get Member | `GET /member-user/{id}` | `MEMBER_USER_READ` | Retrieves a specific member user. |
| Get All Members | `GET /member-user` | `MEMBER_USER_READ` | Retrieves a list of all members. |
| Delete Member | `DELETE /member-user/{id}` | `MEMBER_USER_DELETE` | Removes a member user from the platform. |

## Organizations
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Organization | `POST /organizations` | `ORGANIZATION_CREATE` | Creates a new organization/company profile. |
| Update Organization | `PUT /organizations` | `ORGANIZATION_UPDATE` | Updates organization details like name or slug. |
| Get Organization | `GET /organizations/{id}` | `ORGANIZATION_READ` | Fetches a specific organization by ID. |
| Get All Organizations | `GET /organizations` | `ORGANIZATION_READ` | Retrieves all registered organizations. |
| Delete Organization | `DELETE /organizations/{id}` | `ORGANIZATION_DELETE` | Removes an organization from the system. |

## Projects
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Project | `POST /projects` | `PROJECT_CREATE` | Creates a new project within an organization. |
| Update Project | `PUT /projects` | `PROJECT_UPDATE` | Updates project settings (status, visibility). |
| Get Project | `GET /projects/{id}` | `PROJECT_READ` | Retrieves a specific project. |
| Get All Projects | `GET /projects` | `PROJECT_READ` | Retrieves all projects. |
| Delete Project | `DELETE /projects/{id}` | `PROJECT_DELETE` | Deletes a project. |

## Sprints
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Sprint | `POST /sprints` | `SPRINT_CREATE` | Creates a sprint within a specific project. |
| Update Sprint | `PUT /sprints` | `SPRINT_UPDATE` | Updates sprint capacity and story points. |
| Get Sprint | `GET /sprints/{id}` | `SPRINT_READ` | Retrieves sprint details. |
| Get All Sprints | `GET /sprints` | `SPRINT_READ` | Retrieves all sprints globally. |
| Delete Sprint | `DELETE /sprints/{id}` | `SPRINT_DELETE` | Deletes a sprint. |

## Roles
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Role | `POST /roles` | `ROLE_CREATE` | Creates a custom role and maps global permissions. |
| Update Role | `PUT /roles` | `ROLE_UPDATE` | Updates the permissions attached to a role. |
| Get Role | `GET /roles/{id}` | `ROLE_READ` | Retrieves a role and its attached permissions. |
| Get All Roles | `GET /roles` | `ROLE_READ` | Retrieves all global roles. |
| Delete Role | `DELETE /roles/{id}` | `ROLE_DELETE` | Deletes a custom role. |

## Project Roles
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Project Role | `POST /project-roles` | `PROJECT_ROLE_CREATE` | Creates a project-level role (e.g., Scrum Master). |
| Update Project Role | `PUT /project-roles` | `PROJECT_ROLE_UPDATE` | Updates a project-level role definition. |
| Get Project Role | `GET /project-roles/{id}` | `PROJECT_ROLE_READ` | Retrieves a specific project role. |
| Get All Project Roles | `GET /project-roles` | `PROJECT_ROLE_READ` | Retrieves all project roles. |
| Delete Project Role | `DELETE /project-roles/{id}` | `PROJECT_ROLE_DELETE` | Deletes a project-level role. |

## Project Users
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Project User | `POST /project-users` | `PROJECT_USER_CREATE` | Assigns a user to a project with a specific Project Role. |
| Delete Project User | `DELETE /project-users/{id}` | `PROJECT_USER_DELETE` | Unassigns/removes a user from a project. |

## Task Types
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Task Type | `POST /task-types` | `TASK_TYPE_CREATE` | Defines a new type of task (e.g., Bug, Epic) for a project workflow. |
| Delete Task Type | `DELETE /task-types/{id}` | `TASK_TYPE_DELETE` | Removes a task type definition. |

## Tasks
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Task | `POST /tasks` | `TASK_CREATE` | Creates a new task ticket, linking it to a project, sprint, type, and assignee. |
| Delete Task | `DELETE /tasks/{id}` | `TASK_DELETE` | Deletes a task from the system. |

## Workflows
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Workflow | `POST /workflows` | `WORKFLOW_CREATE` | Creates a new workflow (e.g., Default Task Workflow). |
| Update Workflow | `PUT /workflows` | `WORKFLOW_UPDATE` | Updates an existing workflow definition. |
| Get Workflow | `GET /workflows/{id}` | `WORKFLOW_READ` | Retrieves a specific workflow. |
| Get All Workflows | `GET /workflows` | `WORKFLOW_READ` | Retrieves all workflows. |
| Delete Workflow | `DELETE /workflows/{id}` | `WORKFLOW_DELETE` | Removes a workflow definition. |

## Workflow States
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Workflow State | `POST /workflow-states` | `WORKFLOW_STATE_CREATE` | Creates a stage/column inside a workflow (e.g., OPEN, IN_PROGRESS). |
| Update Workflow State | `PUT /workflow-states` | `WORKFLOW_STATE_UPDATE` | Updates a workflow state (name, sequence order). |
| Get Workflow State | `GET /workflow-states/{id}` | `WORKFLOW_STATE_READ` | Retrieves a specific workflow state. |
| Get All Workflow States | `GET /workflow-states` | `WORKFLOW_STATE_READ` | Retrieves all workflow states. |
| Delete Workflow State | `DELETE /workflow-states/{id}` | `WORKFLOW_STATE_DELETE` | Removes a workflow state. |

## Workflow Transitions
| API Name | Endpoint | @RequirePermission | Explanation |
|---|---|---|---|
| Create Workflow Transition | `POST /workflow-transitions` | `WORKFLOW_TRANSITION_CREATE` | Defines an allowed movement between two states (e.g., OPEN -> IN_PROGRESS). |
| Update Workflow Transition | `PUT /workflow-transitions` | `WORKFLOW_TRANSITION_UPDATE` | Updates an existing state transition rule. |
| Get Workflow Transition | `GET /workflow-transitions/{id}` | `WORKFLOW_TRANSITION_READ` | Retrieves a specific transition rule. |
| Get All Workflow Transitions | `GET /workflow-transitions` | `WORKFLOW_TRANSITION_READ` | Retrieves all transition rules. |
| Delete Workflow Transition | `DELETE /workflow-transitions/{id}` | `WORKFLOW_TRANSITION_DELETE` | Deletes a state transition rule. |
