package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Comment
import com.enihsyou.astolfo.hotel.exception.订单不存在
import com.enihsyou.astolfo.hotel.exception.评论不存在
import com.enihsyou.astolfo.hotel.exception.评论已存在不可修改
import com.enihsyou.astolfo.hotel.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface CommentService {
    fun createComment(transaction: Int, comment: Comment)
    fun showTransactionComment(transaction: Int): Comment
    fun listRoomComment(room: Int): List<Map<String, Any>>
    fun getComment(commentId: Int): Map<String, Any>
}

@Service(value = "评论层逻辑")
class CommentServiceImpl : CommentService {

    override fun getComment(commentId: Int): Map<String, Any> {
        return if (commentRepository.exists(commentId)) {
            val comment = commentRepository.findOne(commentId)
            comment.let {
                mutableMapOf(
                    "id" to it.id,
                    "body" to it.body,
                    "user" to mapOf(
                        "id" to it.user.id,
                        "nickname" to it.user.nickname,
                        "role" to it.user.role.name
                    ),
                    "createdDate" to it.createdDate
                )
            }
        } else throw 评论不存在(commentId)
    }

    override fun showTransactionComment(transaction: Int): Comment {
        return if (transactionRepository.exists(transaction)) {
            val transaction1 = transactionRepository.findOne(transaction)
            if (transaction1.comment != null)
                transaction1.comment!!
            else
                throw 评论不存在(transaction)
        } else throw 订单不存在(transaction)
    }

    override fun listRoomComment(room: Int): List<Map<String, Any>> {
        val findOne = roomRepository.findOne(room)
        val comments = mutableListOf<Map<String, Any>>()
        findOne.comments.map {
            mutableMapOf(
                "id" to it.id,
                "body" to it.body,
                "user" to mapOf(
                    "id" to it.user.id,
                    "nickname" to it.user.nickname,
                    "role" to it.user.role.name
                ),
                "createdDate" to it.createdDate
            )
        }.forEach { comments += it }
        return comments.toList()
    }

    override fun createComment(transaction: Int, comment: Comment) {
        /*先找到订单*/
        val findOne = transactionRepository.findOne(transaction)
        if (findOne.comment != null)
            throw 评论已存在不可修改(transaction)

        /*设置评论的创建者*/
        comment.user = findOne.user

        /*保存评论*/
        val save = commentRepository.save(comment)

        /*修改关系*/
        findOne.comment = save
        findOne.room.comments.add(save)
        transactionRepository.save(findOne)
        roomRepository.save(findOne.room)
    }

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var commentRepository: CommentRepository
    @Autowired lateinit var transactionRepository: TransactionRepository
    @Autowired lateinit var roomRepository: RoomRepository
    @Autowired lateinit var roomTypeRepository: RoomTypeRepository
    @Autowired lateinit var roomDirectionRepository: RoomDirectionRepository
    @Autowired lateinit var guestRepository: GuestRepository
}
