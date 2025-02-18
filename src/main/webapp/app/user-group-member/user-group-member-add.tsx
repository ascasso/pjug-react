import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { UserGroupMemberDTO } from 'app/user-group-member/user-group-member-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    firstName: yup.string().emptyToNull().max(255).required(),
    lastName: yup.string().emptyToNull().max(255).required(),
    email: yup.string().emptyToNull().max(255).required(),
    preferredJDK: yup.string().emptyToNull(),
    usergroupId: yup.string().emptyToNull().uuid().required()
  });
}

export default function UserGroupMemberAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('userGroupMember.add.headline'));

  const navigate = useNavigate();
  const [usergroupIdValues, setUsergroupIdValues] = useState<Record<string,string>>({});

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const getMessage = (key: string) => {
    const messages: Record<string, string> = {
      USER_GROUP_MEMBER_EMAIL_UNIQUE: t('exists.userGroupMember.email')
    };
    return messages[key];
  };

  const prepareRelations = async () => {
    try {
      const usergroupIdValuesResponse = await axios.get('/api/userGroupMembers/usergroupIdValues');
      setUsergroupIdValues(usergroupIdValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createUserGroupMember = async (data: UserGroupMemberDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/userGroupMembers', data);
      navigate('/userGroupMembers', {
            state: {
              msgSuccess: t('userGroupMember.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t, getMessage);
    }
  };

  return (<>
    <div className="d-flex flex-wrap mb-4">
      <h1 className="flex-grow-1">{t('userGroupMember.add.headline')}</h1>
      <div>
        <Link to="/userGroupMembers" className="btn btn-secondary">{t('userGroupMember.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createUserGroupMember)} noValidate>
      <InputRow useFormResult={useFormResult} object="userGroupMember" field="firstName" required={true} />
      <InputRow useFormResult={useFormResult} object="userGroupMember" field="lastName" required={true} />
      <InputRow useFormResult={useFormResult} object="userGroupMember" field="email" required={true} />
      <InputRow useFormResult={useFormResult} object="userGroupMember" field="preferredJDK" type="select" options={{'JDK17': 'JDK17', 'JDK21': 'JDK21', 'JDK1_8': 'JDK1_8', 'JDK11': 'JDK11', 'JDK23': 'JDK23'}} />
      <InputRow useFormResult={useFormResult} object="userGroupMember" field="usergroupId" required={true} type="select" options={usergroupIdValues} />
      <input type="submit" value={t('userGroupMember.add.headline')} className="btn btn-primary mt-4" />
    </form>
  </>);
}
