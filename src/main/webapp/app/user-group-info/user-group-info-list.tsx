import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { UserGroupInfoDTO } from 'app/user-group-info/user-group-info-model';
import { PagedModel, Pagination } from 'app/common/list-helper/pagination';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function UserGroupInfoList() {
  const { t } = useTranslation();
  useDocumentTitle(t('userGroupInfo.list.headline'));

  const [userGroupInfoes, setUserGroupInfoes] = useState<PagedModel<UserGroupInfoDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('userGroupInfo.list.sort.id,ASC'), 
    'groupID,ASC': t('userGroupInfo.list.sort.groupID,ASC'), 
    'groupName,ASC': t('userGroupInfo.list.sort.groupName,ASC')
  };

  const getAllUserGroupInfoes = async () => {
    try {
      const response = await axios.get('/api/userGroupInfos?' + listParams);
      setUserGroupInfoes(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: string) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/userGroupInfos/' + id);
      navigate('/userGroupInfos', {
            state: {
              msgInfo: t('userGroupInfo.delete.success')
            }
          });
      getAllUserGroupInfoes();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/userGroupInfos', {
              state: {
                msgError: t(messageParts[0]!, { id: messageParts[1]! })
              }
            });
        return;
      }
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllUserGroupInfoes();
  }, [searchParams]);

  return (<>
    <div className="d-flex flex-wrap mb-4">
      <h1 className="flex-grow-1">{t('userGroupInfo.list.headline')}</h1>
      <div>
        <Link to="/userGroupInfos/add" className="btn btn-primary ms-2">{t('userGroupInfo.list.createNew')}</Link>
      </div>
    </div>
    {((userGroupInfoes && userGroupInfoes.page.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="row">
      <SearchFilter placeholder={t('userGroupInfo.list.filter')} />
      <Sorting sortOptions={sortOptions} rowClass="offset-lg-4" />
    </div>
    )}
    {!userGroupInfoes || userGroupInfoes.page.totalElements === 0 ? (
    <div>{t('userGroupInfo.list.empty')}</div>
    ) : (<>
    <div className="table-responsive">
      <table className="table table-striped table-hover align-middle">
        <thead>
          <tr>
            <th scope="col">{t('userGroupInfo.id.label')}</th>
            <th scope="col">{t('userGroupInfo.groupID.label')}</th>
            <th scope="col">{t('userGroupInfo.groupName.label')}</th>
            <th scope="col">{t('userGroupInfo.isActive.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {userGroupInfoes.content.map((userGroupInfo) => (
          <tr key={userGroupInfo.id}>
            <td>{userGroupInfo.id}</td>
            <td>{userGroupInfo.groupID}</td>
            <td>{userGroupInfo.groupName}</td>
            <td>{userGroupInfo.isActive?.toString()}</td>
            <td>
              <div className="float-end text-nowrap">
                <Link to={'/userGroupInfos/edit/' + userGroupInfo.id} className="btn btn-sm btn-secondary">{t('userGroupInfo.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(userGroupInfo.id!)} className="btn btn-sm btn-secondary">{t('userGroupInfo.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={userGroupInfoes.page} />
    </>)}
  </>);
}
