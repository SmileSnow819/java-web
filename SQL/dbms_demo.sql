/*
 Navicat Premium Dump SQL

 Source Server         : db
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : dbms_demo

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 07/01/2026 21:04:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `stuNo` int NOT NULL AUTO_INCREMENT,
  `stuName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `stuAge` int NOT NULL,
  `stuImg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`stuNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (26, '刘七', 22, NULL);
INSERT INTO `student` VALUES (27, '张八', 18, NULL);
INSERT INTO `student` VALUES (28, '李九', 19, NULL);
INSERT INTO `student` VALUES (29, '王十', 20, NULL);
INSERT INTO `student` VALUES (30, '赵一', 21, NULL);
INSERT INTO `student` VALUES (31, '钱二', 22, NULL);
INSERT INTO `student` VALUES (32, '孙三1', 18, NULL);
INSERT INTO `student` VALUES (33, '周四', 19, NULL);
INSERT INTO `student` VALUES (34, '吴五', 20, NULL);
INSERT INTO `student` VALUES (35, '郑六', 21, NULL);
INSERT INTO `student` VALUES (36, '王七', 22, NULL);
INSERT INTO `student` VALUES (39, '王213饿3', 18, NULL);
INSERT INTO `student` VALUES (40, '王13', 18, NULL);
INSERT INTO `student` VALUES (41, '李明1', 18, NULL);
INSERT INTO `student` VALUES (42, '杨2134', 18, NULL);
INSERT INTO `student` VALUES (43, '杨1234', 18, NULL);
INSERT INTO `student` VALUES (44, '******', 18, NULL);
INSERT INTO `student` VALUES (45, 'admin', 18, NULL);
INSERT INTO `student` VALUES (47, '******', 18, NULL);
INSERT INTO `student` VALUES (48, 'admin', 18, NULL);
INSERT INTO `student` VALUES (49, '******', 18, NULL);
INSERT INTO `student` VALUES (50, '******', 18, NULL);
INSERT INTO `student` VALUES (52, '*****', 18, NULL);
INSERT INTO `student` VALUES (54, '**', 16, NULL);
INSERT INTO `student` VALUES (55, '**', 18, NULL);
INSERT INTO `student` VALUES (56, '杨思梦', 21, 'images/d28499ff-f871-4375-9f74-16b045892620.png');
INSERT INTO `student` VALUES (57, '测试数据123', 22, 'images/05660089-075d-4974-ae26-f4d32242de0d.ico');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `u_id` int NOT NULL AUTO_INCREMENT,
  `u_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `u_pwd` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`u_id`) USING BTREE,
  UNIQUE INDEX `u_name`(`u_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '123456', '123456');
INSERT INTO `user` VALUES (2, '1234567', '1234567');
INSERT INTO `user` VALUES (7, '123456789', '123456789');
INSERT INTO `user` VALUES (10, 'meng', '123456');
INSERT INTO `user` VALUES (12, '1234567891', '12345678911');
INSERT INTO `user` VALUES (13, '666666', '666666');
INSERT INTO `user` VALUES (15, '6666661', '6666661');
INSERT INTO `user` VALUES (17, '66666612', '66666612');
INSERT INTO `user` VALUES (18, '123456790', '123456790');
INSERT INTO `user` VALUES (19, '1234566', '1234566');
INSERT INTO `user` VALUES (20, '12345678911', '12345678911');
INSERT INTO `user` VALUES (21, '12345678111', '12345678111');
INSERT INTO `user` VALUES (22, '1111111111111111', '1111111111111111');
INSERT INTO `user` VALUES (24, '1234567111', '1234567111');
INSERT INTO `user` VALUES (25, '111111113131', '111111113131');
INSERT INTO `user` VALUES (27, '123141414141', '123141414141');
INSERT INTO `user` VALUES (28, '1111313131', '1111313131');
INSERT INTO `user` VALUES (29, 'test1234', 'test1234');
INSERT INTO `user` VALUES (30, 'meng1234', 'meng1234');

SET FOREIGN_KEY_CHECKS = 1;
