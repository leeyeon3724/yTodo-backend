package com.yeon.ytodo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class YTodoAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private String filePath;

    // 첨부파일이 속하는 엔티티의 ID
    @Column(nullable = false)
    private Long entityId;

    public YTodoAttachment(String fileName, String fileType, String filePath, Long entityId) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.entityId = entityId;
    }
}
