package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.Comment
import com.enihsyou.astolfo.hotel.service.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController("评论接口控制器")
@RequestMapping("/api/comments")
class CommentController {

    @Autowired lateinit var commentService: CommentService

    /*评论系统*/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(@RequestParam transactionId: Int, @RequestBody comment: Comment)
        = commentService.createComment(transactionId, comment)

    @GetMapping("/transaction")
    fun showTransactionComment(@RequestParam transactionId: Int)
        = commentService.showTransactionComment(transactionId)

    @GetMapping("/room")
    fun showRoomComment(@RequestParam roomId: Int)
        = commentService.listRoomComment(roomId)

    @GetMapping
    fun getComment(@RequestParam commentId: Int)
        = commentService.getComment(commentId)

}

