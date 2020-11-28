import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/components/LoginPage.vue'
import Main from '@/components/MainPage.vue'
import Applicant from '@/components/ApplicantPage.vue'
import OngoingInterview from '@/components/OngoingInterviewPage.vue'
import FinishedInterview from '@/components/FinishedInterviewPage.vue'
import Page404 from '@/components/Page404.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/main',
    name: 'Main',
    component: Main
  },
  {
    path: '/applicant',
    name: 'Applicant',
    component: Applicant
  },
  {
    path: '/ongoing_interview',
    name: 'OngoingInterview',
    component: OngoingInterview
  },
  {
    path: '/finished_interview',
    name: 'FinishedInterview',
    component: FinishedInterview
  },
  {
    path: '/404',
    name: 'Page404',
    component: Page404
  },
  {
    path: '*',
    redirect: '/404'
  }
]

const router = new VueRouter({
  // mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
