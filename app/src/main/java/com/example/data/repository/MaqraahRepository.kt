package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.model.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class MaqraahRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    suspend fun getUserProfile(uid: String): UserProfile? {
        return try {
            val doc = db.collection("users").document(uid).get().await()
            doc.toObject(UserProfile::class.java)
        } catch (e: Exception) {
            Log.e("MaqraahRepo", "Error getting user profile", e)
            null
        }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).set(profile.copy(id = uid)).await()
    }

    fun observeAllUsers(): Flow<List<UserProfile>> {
        return db.collection("users")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(UserProfile::class.java) }
    }

    suspend fun updateUserRole(userId: String, newRole: String) {
        db.collection("users").document(userId).update("role", newRole).await()
    }

    suspend fun blockUser(userId: String, blocked: Boolean) {
        db.collection("users").document(userId).update("isBlocked", blocked).await()
    }

    suspend fun addBonusMinutes(userId: String, minutes: Int) {
        val userRef = db.collection("users").document(userId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentBalance = (snapshot.getLong("minuteBalance") ?: 0L).toInt()
            transaction.update(userRef, "minuteBalance", currentBalance + minutes)
        }.await()
    }

    fun observeStudyGroups(): Flow<List<StudyGroup>> {
        return db.collection("study_groups")
            .snapshots()
            .map { snapshot ->
                val list = snapshot.toObjects(StudyGroup::class.java)
                if (list.isEmpty()) {
                    initializeDefaultGroups()
                }
                list
            }
    }

    private fun initializeDefaultGroups() {
        val defaultGroups = listOf(
            StudyGroup("g1", "ሴቶች - የጧት ፈረቃ ቡድን", "نساء - فترة الصباح", "ሴት", "ጧት", "Female Morning Shift Group", "ኡስታዛ ፋጢማ", 12),
            StudyGroup("g2", "ሴቶች - የከሰአት ፈረቃ ቡድን", "نساء - فترة الظهر", "ሴት", "ከሰአት", "Female Afternoon Shift Group", "ኡስታዛ ዘይነብ", 9),
            StudyGroup("g3", "ሴቶች - የማታ ፈረቃ ቡድን", "نساء - فترة المساء", "ሴት", "ማታ", "Female Night Shift Group", "ኡስታዛ አኢሻ", 15),
            StudyGroup("g4", "ወንዶች - የጧት ፈረቃ ቡድን", "رجال - فترة الصباح", "ወንድ", "ጧት", "Male Morning Shift Group", "ሼህ አህመድ", 18),
            StudyGroup("g5", "ወንዶች - የከሰአት ፈረቃ ቡድን", "رجال - فترة الظهر", "ወንድ", "ከሰአት", "Male Afternoon Shift Group", "ሙቅሪዕ ሐሰን", 14),
            StudyGroup("g6", "ወንዶች - የማታ ፈረቃ ቡድን", "رجال - فترة المساء", "ወንድ", "ማታ", "Male Night Shift Group", "ዶ/ር ሙአዝ", 20),
            StudyGroup("g7", "ሙልተቃ አል ኣም (ዋና መገናኛ)", "الملتقى العام", "All", "All", "General Hub for All Students & Teachers", "ዋና አስተዳዳሪ", 75)
        )
        for (g in defaultGroups) {
            db.collection("study_groups").document(g.id).set(g)
        }
    }

    fun observeStories(): Flow<List<StoryItem>> {
        return db.collection("stories")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(StoryItem::class.java) }
    }

    suspend fun publishStory(story: StoryItem) {
        db.collection("stories").add(story).await()
    }

    fun observeFriendshipRequests(userId: String): Flow<List<FriendshipRequest>> {
        return db.collection("friendship_requests")
            .whereEqualTo("receiverId", userId)
            .snapshots()
            .map { snapshot -> snapshot.toObjects(FriendshipRequest::class.java) }
    }

    suspend fun sendFriendshipRequest(req: FriendshipRequest) {
        db.collection("friendship_requests").add(req).await()
    }

    suspend fun updateFriendshipStatus(requestId: String, status: String) {
        db.collection("friendship_requests").document(requestId).update("status", status).await()
    }

    fun observeProgramChangeRequests(): Flow<List<ProgramChangeRequest>> {
        return db.collection("program_change_requests")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(ProgramChangeRequest::class.java) }
    }

    suspend fun submitProgramChangeRequest(req: ProgramChangeRequest) {
        db.collection("program_change_requests").add(req).await()
    }

    suspend fun updateProgramChangeStatus(requestId: String, userId: String, newProgram: String, status: String) {
        db.collection("program_change_requests").document(requestId).update("status", status).await()
        if (status == "Approved" && userId.isNotBlank()) {
            db.collection("users").document(userId).update("programType", newProgram).await()
        }
    }

    fun observeAnnouncements(): Flow<List<AnnouncementItem>> {
        return db.collection("announcements")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(AnnouncementItem::class.java) }
    }

    suspend fun postAnnouncement(item: AnnouncementItem) {
        db.collection("announcements").add(item).await()
    }

    fun observeClasses(): Flow<List<LiveClassItem>> {
        return db.collection("classes")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(LiveClassItem::class.java) }
    }

    suspend fun scheduleClass(item: LiveClassItem) {
        db.collection("classes").add(item).await()
    }

    fun observeMessages(chatId: String = "general"): Flow<List<ChatMessage>> {
        return db.collection("messages")
            .whereEqualTo("chatId", chatId)
            .snapshots()
            .map { snapshot -> snapshot.toObjects(ChatMessage::class.java).sortedBy { it.timestamp } }
    }

    suspend fun sendMessage(message: ChatMessage) {
        db.collection("messages").add(message).await()
    }

    fun observePaymentRequests(): Flow<List<PaymentRequestItem>> {
        return db.collection("payment_requests")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(PaymentRequestItem::class.java) }
    }

    suspend fun submitPaymentRequest(request: PaymentRequestItem) {
        db.collection("payment_requests").add(request).await()
    }

    suspend fun updatePaymentStatus(requestId: String, status: String, userId: String, minutesToAdd: Int) {
        db.collection("payment_requests").document(requestId).update("status", status).await()
        if (status == "Approved" && userId.isNotBlank() && minutesToAdd > 0) {
            val userRef = db.collection("users").document(userId)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val currentBalance = (snapshot.getLong("minuteBalance") ?: 0L).toInt()
                transaction.update(userRef, "minuteBalance", currentBalance + minutesToAdd)
            }.await()
        }
    }
}
