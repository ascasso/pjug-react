import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { UserGroupMemberDTO } from 'app/user-group-member/user-group-member-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function UserGroupMemberList() {
  const { t } = useTranslation();
  useDocumentTitle(t('userGroupMember.list.headline'));

  const [userGroupMembers, setUserGroupMembers] = useState<ListModel<UserGroupMemberDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('userGroupMember.list.sort.id,ASC'), 
    'firstName,ASC': t('userGroupMember.list.sort.firstName,ASC'), 
    'lastName,ASC': t('userGroupMember.list.sort.lastName,ASC')
  };

  const getAllUserGroupMembers = async () => {
    try {
      const response = await axios.get('/api/userGroupMembers?' + listParams);
      setUserGroupMembers(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: string) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/userGroupMembers/' + id);
      navigate('/userGroupMembers', {
            state: {
              msgInfo: t('userGroupMember.delete.success')
            }
          });
      getAllUserGroupMembers();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllUserGroupMembers();
  }, [searchParams]);

  return (<>
    <div className="d-flex flex-wrap mb-4">
      <h1 className="flex-grow-1">{t('userGroupMember.list.headline')}</h1>
      <div>
        <Link to="/userGroupMembers/add" className="btn btn-primary ms-2">{t('userGroupMember.list.createNew')}</Link>
      </div>
    </div>
    {((userGroupMembers?._embedded && userGroupMembers?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="row">
      <SearchFilter placeholder={t('userGroupMember.list.filter')} />
      <Sorting sortOptions={sortOptions} rowClass="offset-lg-4" />
    </div>
    )}
    {!userGroupMembers?._embedded || userGroupMembers?.page?.totalElements === 0 ? (
    <div>{t('userGroupMember.list.empty')}</div>
    ) : (<>
    <div className="table-responsive">
      <table className="table table-striped table-hover align-middle">
        <thead>
          <tr>
            <th scope="col">{t('userGroupMember.id.label')}</th>
            <th scope="col">{t('userGroupMember.firstName.label')}</th>
            <th scope="col">{t('userGroupMember.lastName.label')}</th>
            <th scope="col">{t('userGroupMember.email.label')}</th>
            <th scope="col">{t('userGroupMember.preferredJDK.label')}</th>
            <th scope="col">{t('userGroupMember.usergroupId.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {userGroupMembers?._embedded?.['userGroupMemberDTOList']?.map((userGroupMember) => (
          <tr key={userGroupMember.id}>
            <td>{userGroupMember.id}</td>
            <td>{userGroupMember.firstName}</td>
            <td>{userGroupMember.lastName}</td>
            <td>{userGroupMember.email}</td>
            <td>{userGroupMember.preferredJDK}</td>
            <td>{userGroupMember.usergroupId}</td>
            <td>
              <div className="float-end text-nowrap">
                <Link to={'/userGroupMembers/edit/' + userGroupMember.id} className="btn btn-sm btn-secondary">{t('userGroupMember.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(userGroupMember.id!)} className="btn btn-sm btn-secondary">{t('userGroupMember.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={userGroupMembers?.page} />
    </>)}
  </>);
}
