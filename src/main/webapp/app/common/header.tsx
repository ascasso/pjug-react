import React from 'react';
import { Link } from 'react-router';
import { useTranslation } from 'react-i18next';
import useAuthentication from 'app/security/use-authentication';


export default function Header() {
  const { t } = useTranslation();
  const authenticationContext = useAuthentication();

  return (
    <header className="bg-light">
      <div className="container">
        <nav className="navbar navbar-light navbar-expand-md">
          <Link to="/" className="navbar-brand">
            <img src="/images/logo.png" alt={t('app.title')} width="30" height="30" className="d-inline-block align-top" />
            <span className="ps-1">{t('app.title')}</span>
          </Link>
          <button type="button" className="navbar-toggler" data-bs-toggle="collapse" data-bs-target="#navbarToggle"
              aria-label={t('navigation.toggle')} aria-controls="navbarToggle" aria-expanded="false">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarToggle">
            <ul className="navbar-nav ms-auto">
              <li className="navbar-item">
                <Link to="/" className="nav-link">{t('navigation.home')}</Link>
              </li>
              <li className="navbar-item dropdown">
                <button type="button" className="nav-link dropdown-toggle" data-bs-toggle="dropdown" id="navbarEntitiesLink"
                    aria-expanded="false">{t('navigation.entities')}</button>
                <ul className="dropdown-menu dropdown-menu-end" aria-labelledby="navbarEntitiesLink">
                  <li><Link to="/userGroupInfos" className="dropdown-item">{t('userGroupInfo.list.headline')}</Link></li>
                  <li><Link to="/userGroupMembers" className="dropdown-item">{t('userGroupMember.list.headline')}</Link></li>
                  <li><Link to="/groupMeetings" className="dropdown-item">{t('groupMeeting.list.headline')}</Link></li>
                </ul>
              </li>
              <li className="navbar-item">
                {authenticationContext.isLoggedIn() ? (
                <button type="button" onClick={() => authenticationContext.logout()} className="nav-link">{t('navigation.logout')}</button>
                ) : (
                <button type="button" onClick={() => authenticationContext.login()} className="nav-link">{t('navigation.login')}</button>
                )}
              </li>
            </ul>
          </div>
        </nav>
      </div>
    </header>
  );
}
