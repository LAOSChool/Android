package com.laoschool.model;

import com.laoschool.entities.*;
import com.laoschool.entities.Class;

import java.util.List;

/**
 * Created by Tran An on 14/03/2016.
 */
public interface DataAccessInterface {

    //----- Authentication -----//

    /**
     * Login to LaoSchool server
     * @param sso_id
     * @param password
     * @param callback return <b>auth_key</b> if success,
     */
    public void login(String sso_id, String password, AsyncCallback<String> callback);

    /**
     * User  change login password himself
     * @param sso_id
     * @param phone
     * @param callback return <b>Success</b> if ok,
     */
    public void forgotPass(String sso_id, String phone, AsyncCallback<String> callback);

    //----- User -----//

    /**
     * Lấy danh sách Users  (header) của một School.
     * - Role: Admin
     * - Pagination: Yes ( tham khảo Common => Pagination )
     * @param filter_class_id (optional)
     * @param filter_user_role (optional)
     * @param filter_sts (optional)
     * @param callback return list users if success,
     */
    public void getUsers(int filter_class_id, int filter_user_role, int filter_sts, AsyncCallback<List<User>> callback);

    /**
     * Get current user information.
     * - Role: (User) Any authenticated User
     * @param callback return current user if success,
     */
    public void getUserProfile(AsyncCallback<User> callback);

    /**
     * Get user information of  a specific ID.
     * - Role: Admin, Teacher
     * @param user_id
     * @param callback return user match the id if success,
     */
    public void getUserById(int user_id, AsyncCallback<User> callback);

   //----- Attendance -----//

    /**
     * Get the list of available attendances.
     * Paging Support: Yes (refer to Common=> Pagination)
     * @param callback return list attendances if success,
     */
    public void getAttendances(AsyncCallback<List<Attendance>> callback);

    /**
     * Get attendance for a specific ID
     * @param attendance_id
     * @param callback return attendance match the id if success,
     */
    public void getAttendanceById(int attendance_id, AsyncCallback<Attendance> callback);

    /**
     * Modify an existing Attendance information.
     * @param attendance
     * @param callback return update attendance if success,
     */
    public void updateAttendance(Attendance attendance, AsyncCallback<Attendance> callback);

    //----- ExamResult -----//

    /**
     * Get the list of available exam_results
     * Paging support: Yes (refer to Common=>Pagination)
     * @param filter_class_id (optional)
     * @param filter_user_id (optional)
     * @param callback return list of exam result if success,
     */
    public void getExamResults(int filter_class_id, int filter_user_id, AsyncCallback<List<ExamResult>> callback);

    /**
     * Get class for a specific Exam Result ID
     * @param exam_id
     * @param callback return exam result if success,
     */
    public void getExamResultById(int exam_id, AsyncCallback<ExamResult> callback);

    //----- FinalResult -----//

    /**
     * Get the list of available final results
     * Paging support: Yes (refer to Common=>Pagination)
     * @param filter_class_id (optional)
     * @param filter_user_id (optional)
     * @param callback return list of final result if success,
     */
    public void getFinalResults(int filter_class_id, int filter_user_id, AsyncCallback<List<FinalResult>> callback);

    /**
     * Get Final Result for a specific ID
     * @param final_id
     * @param callback return final result if success,
     */
    public void getFinalResultById(int final_id, AsyncCallback<FinalResult> callback);

    //----- TimeTable -----//

    /**
     * Get list of timetables belong to a school
     * Pagination: Yes ( tham khảo Common => Pagination )
     * @param filter_class_id (optional)
     * @param callback return list of timetable if success,
     */
    public void getTimeTables(int filter_class_id, AsyncCallback<List<TimeTable>> callback);

    /**
     * Modify an existing Timetable information.
     * @param timetable
     * @param callback return updated timetable if success,
     */
    public void updateTimeTable(TimeTable timetable, AsyncCallback<TimeTable> callback);

    //----- Message -----//

    /**
     * Get the list of available messages
     * Pagination: Yes ( tham khảo Common => Pagination )
     * @param filter_class_id (optional)
     * @param filter_from_user_id (optional)
     * @param filter_from_dt (optional)
     * @param filter_to_dt (optional)
     * @param filter_to_user_id (optional)
     * @param filter_channel (optional)
     * @param filter_sts (optional)
     * @param callback return list of messages if success,
     */
    public void getMessages(String filter_class_id, String filter_from_user_id, String filter_from_dt, String filter_to_dt, String filter_to_user_id, String filter_channel, String filter_sts,String filter_from_id, AsyncCallback<List<Message>> callback);

    /**
     * Get Message for a specific Message ID
     * @param message_id
     * @param callback return the message if success,
     */
    public void getMessageById(int message_id, AsyncCallback<Message> callback);

    /**
     * Create new Message
     * @param message
     * @param callback return new message,
     */
    public void createMessage(Message message, AsyncCallback<Message> callback);

    /**
     * Modify existing Message
     * @param message
     * @param callback return update message if success,
     */
    public void updateMessage(Message message, AsyncCallback<Message> callback);

    /**
     * Delete an existing Message
     * @param message_id
     * @param callback return delete success message
     */
    public void deleteMessage(int message_id, AsyncCallback<String> callback);

    //----- School -----//

    /**
     * Get school for a specific school ID
     * @param school_id
     * @param callback return the school if success,
     */
    public void getSchoolById(int school_id, AsyncCallback<School> callback);

    //----- Class -----//

    /**
     * Get class for a specific class ID
     * @param class_id
     * @param callback return the class if success,
     */
    public void getClassById(int class_id, AsyncCallback<Class> callback);
}
