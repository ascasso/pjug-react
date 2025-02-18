import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { UserGroupInfoDTO } from 'app/user-group-info/user-group-info-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    groupID: yup.string().emptyToNull().max(30).required(),
    groupName: yup.string().emptyToNull().max(60).required(),
    isActive: yup.bool()
  });
}

export default function UserGroupInfoEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('userGroupInfo.edit.headline'));

  const navigate = useNavigate();
  const params = useParams();
  const currentId = params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const getMessage = (key: string) => {
    const messages: Record<string, string> = {
      USER_GROUP_INFO_GROUP_IDUNIQUE: t('exists.userGroupInfo.groupID')
    };
    return messages[key];
  };

  const prepareForm = async () => {
    try {
      const data = (await axios.get('/api/userGroupInfos/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateUserGroupInfo = async (data: UserGroupInfoDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/userGroupInfos/' + currentId, data);
      navigate('/userGroupInfos', {
            state: {
              msgSuccess: t('userGroupInfo.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t, getMessage);
    }
  };

  return (<>
    <div className="d-flex flex-wrap mb-4">
      <h1 className="flex-grow-1">{t('userGroupInfo.edit.headline')}</h1>
      <div>
        <Link to="/userGroupInfos" className="btn btn-secondary">{t('userGroupInfo.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateUserGroupInfo)} noValidate>
      <InputRow useFormResult={useFormResult} object="userGroupInfo" field="id" disabled={true} />
      <InputRow useFormResult={useFormResult} object="userGroupInfo" field="groupID" required={true} />
      <InputRow useFormResult={useFormResult} object="userGroupInfo" field="groupName" required={true} />
      <InputRow useFormResult={useFormResult} object="userGroupInfo" field="isActive" type="checkbox" />
      <input type="submit" value={t('userGroupInfo.edit.headline')} className="btn btn-primary mt-4" />
    </form>
  </>);
}
