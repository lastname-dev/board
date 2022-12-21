package com.example.board.post;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String title;
    String Content;
    String user_id="aa";
    @Enumerated(EnumType.STRING)
    Kind kind;

    @Builder
    public Post(Integer id, String title, String content, String user_id, Kind kind) {
        this.id = id;
        this.title = title;
        Content = content;
        this.user_id = user_id;
        this.kind = kind;
    }

    public PostDto toDto() {
        return new PostDto(id,title,Content,user_id,kind,getWrittenDate());
    }
}
