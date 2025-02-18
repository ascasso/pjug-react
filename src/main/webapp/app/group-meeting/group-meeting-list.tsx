import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { GroupMeetingDTO } from 'app/group-meeting/group-meeting-model';
import { PagedModel, Pagination } from 'app/common/list-helper/pagination';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function GroupMeetingList() {
  const { t } = useTranslation();
  useDocumentTitle(t('groupMeeting.list.headline'));

  const [groupMeetings, setGroupMeetings] = useState<PagedModel<GroupMeetingDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('groupMeeting.list.sort.id,ASC'), 
    'location,ASC': t('groupMeeting.list.sort.location,ASC'), 
    'meetingTopic,ASC': t('groupMeeting.list.sort.meetingTopic,ASC')
  };

  const getAllGroupMeetings = async () => {
    try {
      const response = await axios.get('/api/groupMeetings?' + listParams);
      setGroupMeetings(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: string) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/groupMeetings/' + id);
      navigate('/groupMeetings', {
            state: {
              msgInfo: t('groupMeeting.delete.success')
            }
          });
      getAllGroupMeetings();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllGroupMeetings();
  }, [searchParams]);

  return (<>
    <div className="d-flex flex-wrap mb-4">
      <h1 className="flex-grow-1">{t('groupMeeting.list.headline')}</h1>
      <div>
        <Link to="/groupMeetings/add" className="btn btn-primary ms-2">{t('groupMeeting.list.createNew')}</Link>
      </div>
    </div>
    {((groupMeetings && groupMeetings.page.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="row">
      <SearchFilter placeholder={t('groupMeeting.list.filter')} />
      <Sorting sortOptions={sortOptions} rowClass="offset-lg-4" />
    </div>
    )}
    {!groupMeetings || groupMeetings.page.totalElements === 0 ? (
    <div>{t('groupMeeting.list.empty')}</div>
    ) : (<>
    <div className="table-responsive">
      <table className="table table-striped table-hover align-middle">
        <thead>
          <tr>
            <th scope="col">{t('groupMeeting.id.label')}</th>
            <th scope="col">{t('groupMeeting.location.label')}</th>
            <th scope="col">{t('groupMeeting.meetingTopic.label')}</th>
            <th scope="col">{t('groupMeeting.meetingStartTime.label')}</th>
            <th scope="col">{t('groupMeeting.userGroupId.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {groupMeetings.content.map((groupMeeting) => (
          <tr key={groupMeeting.id}>
            <td>{groupMeeting.id}</td>
            <td>{groupMeeting.location}</td>
            <td>{groupMeeting.meetingTopic}</td>
            <td>{groupMeeting.meetingStartTime}</td>
            <td>{groupMeeting.userGroupId}</td>
            <td>
              <div className="float-end text-nowrap">
                <Link to={'/groupMeetings/edit/' + groupMeeting.id} className="btn btn-sm btn-secondary">{t('groupMeeting.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(groupMeeting.id!)} className="btn btn-sm btn-secondary">{t('groupMeeting.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={groupMeetings.page} />
    </>)}
  </>);
}
