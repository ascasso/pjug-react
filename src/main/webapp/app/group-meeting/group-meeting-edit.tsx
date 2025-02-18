import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { GroupMeetingDTO } from 'app/group-meeting/group-meeting-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    location: yup.string().emptyToNull().max(255),
    meetingTopic: yup.string().emptyToNull().max(255),
    meetingStartTime: yup.string().emptyToNull().offsetDateTime().required(),
    userGroupId: yup.string().emptyToNull().uuid()
  });
}

export default function GroupMeetingEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('groupMeeting.edit.headline'));

  const navigate = useNavigate();
  const [userGroupIdValues, setUserGroupIdValues] = useState<Record<string,string>>({});
  const params = useParams();
  const currentId = params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const userGroupIdValuesResponse = await axios.get('/api/groupMeetings/userGroupIdValues');
      setUserGroupIdValues(userGroupIdValuesResponse.data);
      const data = (await axios.get('/api/groupMeetings/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateGroupMeeting = async (data: GroupMeetingDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/groupMeetings/' + currentId, data);
      navigate('/groupMeetings', {
            state: {
              msgSuccess: t('groupMeeting.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="d-flex flex-wrap mb-4">
      <h1 className="flex-grow-1">{t('groupMeeting.edit.headline')}</h1>
      <div>
        <Link to="/groupMeetings" className="btn btn-secondary">{t('groupMeeting.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateGroupMeeting)} noValidate>
      <InputRow useFormResult={useFormResult} object="groupMeeting" field="id" disabled={true} />
      <InputRow useFormResult={useFormResult} object="groupMeeting" field="location" />
      <InputRow useFormResult={useFormResult} object="groupMeeting" field="meetingTopic" />
      <InputRow useFormResult={useFormResult} object="groupMeeting" field="meetingStartTime" required={true} />
      <InputRow useFormResult={useFormResult} object="groupMeeting" field="userGroupId" type="select" options={userGroupIdValues} />
      <input type="submit" value={t('groupMeeting.edit.headline')} className="btn btn-primary mt-4" />
    </form>
  </>);
}
