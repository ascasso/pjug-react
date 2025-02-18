import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router';
import App from "./app";
import Home from './home/home';
import UserGroupInfoList from './user-group-info/user-group-info-list';
import UserGroupInfoAdd from './user-group-info/user-group-info-add';
import UserGroupInfoEdit from './user-group-info/user-group-info-edit';
import UserGroupMemberList from './user-group-member/user-group-member-list';
import UserGroupMemberAdd from './user-group-member/user-group-member-add';
import UserGroupMemberEdit from './user-group-member/user-group-member-edit';
import GroupMeetingList from './group-meeting/group-meeting-list';
import GroupMeetingAdd from './group-meeting/group-meeting-add';
import GroupMeetingEdit from './group-meeting/group-meeting-edit';
import Error from './error/error';


export default function AppRoutes() {
  const router = createBrowserRouter([
    {
      element: <App />,
      children: [
        { path: '', element: <Home /> },
        { path: 'userGroupInfos', element: <UserGroupInfoList /> },
        { path: 'userGroupInfos/add', element: <UserGroupInfoAdd /> },
        { path: 'userGroupInfos/edit/:id', element: <UserGroupInfoEdit /> },
        { path: 'userGroupMembers', element: <UserGroupMemberList /> },
        { path: 'userGroupMembers/add', element: <UserGroupMemberAdd /> },
        { path: 'userGroupMembers/edit/:id', element: <UserGroupMemberEdit /> },
        { path: 'groupMeetings', element: <GroupMeetingList /> },
        { path: 'groupMeetings/add', element: <GroupMeetingAdd /> },
        { path: 'groupMeetings/edit/:id', element: <GroupMeetingEdit /> },
        { path: 'error', element: <Error /> },
        { path: '*', element: <Error /> }
      ]
    }
  ]);

  return (
    <RouterProvider router={router} />
  );
}
