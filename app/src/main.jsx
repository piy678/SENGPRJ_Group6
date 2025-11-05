
import React from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import App from './App.jsx';
import Landing from './pages/Landing.jsx';
import StudentDashboard from './pages/StudentDashboard.jsx';
import StudentProgress from './pages/StudentProgress.jsx';
import TeacherDashboard from './pages/teacher/TeacherDashboard.jsx';
import AddCourse from './pages/teacher/tabs/AddCourse.jsx'; 

const router = createBrowserRouter([
  { path: '/', element: <App />, children: [
    { index: true, element: <Landing /> },
    { path: 'student', element: <StudentDashboard /> },
    { path: 'student/progress', element: <StudentProgress /> },
    { path: 'teacher', element: <TeacherDashboard /> },
    { path: 'teacher/courses/new', element: <AddCourse /> }, // NEU
  ] }
]);
createRoot(document.getElementById('root')).render(<RouterProvider router={router} />);


/*
import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";

ReactDOM.createRoot(document.getElementById("root")).render(<App />);
*/