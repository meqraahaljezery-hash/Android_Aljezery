package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class UserProfile(
    @DocumentId val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "student", // student, teacher, admin, super_admin
    val studentCode: String = "", // e.g. IM-0001 or IF-0001
    val age: Int = 0,
    val gender: String = "ወንድ (Male)", // ወንድ (Male) / ሴት (Female)
    val city: String = "",
    val phone: String = "",
    val language: String = "am", // am, ar, en
    val quranLevel: String = "መሰረታዊ (Beginner)",
    val programType: String = "ሀፍስ ኢጃዛ (Hafs Ijazah)",
    val shift: String = "የጧት ፈረቃ (Morning)", // ጧት, ከሰአት, ማታ
    val avatarEmoji: String = "🧔🏽‍♂️", // Islamic male/female emoji
    val minuteBalance: Int = 60,
    val isBlocked: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class StudyGroup(
    @DocumentId val id: String = "",
    val name: String = "",
    val nameAr: String = "",
    val genderFilter: String = "All", // ወንድ, ሴት, All
    val shiftFilter: String = "All", // ጧት, ከሰአት, ማታ, All
    val description: String = "",
    val leaderName: String = "",
    val membersCount: Int = 0
)

data class StoryItem(
    @DocumentId val id: String = "",
    val title: String = "",
    val content: String = "",
    val authorName: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class FriendshipRequest(
    @DocumentId val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderCode: String = "",
    val receiverId: String = "",
    val receiverName: String = "",
    val status: String = "Pending", // Pending, Accepted, Rejected
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class ProgramChangeRequest(
    @DocumentId val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val currentProgram: String = "",
    val requestedProgram: String = "",
    val reason: String = "",
    val status: String = "Pending", // Pending, Approved, Rejected
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class LiveClassItem(
    @DocumentId val id: String = "",
    val title: String = "",
    val teacherId: String = "",
    val teacherName: String = "",
    val scheduleTime: String = "",
    val durationMinutes: Int = 45,
    val meetingUrl: String = "",
    val status: String = "Scheduled" // Scheduled, Ongoing, Completed
)

data class ChatMessage(
    @DocumentId val id: String = "",
    val chatId: String = "general",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val audioUrl: String = "",
    @ServerTimestamp val timestamp: Timestamp? = null
)

data class PaymentRequestItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val amountEtb: Int = 500,
    val minutesRequested: Int = 100,
    val paymentMethod: String = "Telebirr",
    val status: String = "Pending", // Pending, Approved, Rejected
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class AnnouncementItem(
    @DocumentId val id: String = "",
    val title: String = "",
    val body: String = "",
    val authorName: String = "Admin",
    @ServerTimestamp val createdAt: Timestamp? = null
)
