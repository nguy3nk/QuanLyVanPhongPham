-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost:3306
-- Thời gian đã tạo: Th1 02, 2020 lúc 06:45 PM
-- Phiên bản máy phục vụ: 5.7.28-0ubuntu0.18.04.4
-- Phiên bản PHP: 7.2.24-0ubuntu0.18.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `qlhh`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Account`
--

CREATE TABLE `Account` (
  `Id` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Phone` varchar(100) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `Mail` varchar(100) NOT NULL,
  `Img` varchar(100) NOT NULL,
  `UserName` varchar(100) NOT NULL,
  `PassWord` varchar(100) NOT NULL,
  `Status` tinyint(4) NOT NULL,
  `Note` varchar(100) NOT NULL,
  `Gender` tinyint(4) NOT NULL,
  `Birthday` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `Account`
--

INSERT INTO `Account` (`Id`, `Name`, `Phone`, `Address`, `Mail`, `Img`, `UserName`, `PassWord`, `Status`, `Note`, `Gender`, `Birthday`) VALUES
(1, 'admin', '123123', '123123', '123123', '/home/khanh/Pictures/RBMH_AgainstTheOdds_AllHeroes_4K.jpg', 'admin', 'admin', 3, '', 0, '2019-12-24 00:00:00'),
(2, 'asd', 'asd', 'asd', 'asd', 'image/', 'khanh', 'asd', 0, 'asd', 0, '2019-01-01 00:00:00'),
(3, 'hiep', 'dienthaoi', 'sad', 'asd', 'test', 'hiep', 'hiep', 1, '', 0, '2019-12-24 00:00:00'),
(4, '', '', '', '', '', 'asd', 'asd', 0, '', 0, '2019-12-31 20:19:36'),
(5, '', '', '', '', '', 'hiep', 'asd', 0, '', 0, '2019-12-31 20:22:57'),
(6, '', '', '', '', '', 'hiep', 'asd', 0, '', 0, '2019-12-31 20:23:05');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Category`
--

CREATE TABLE `Category` (
  `Id` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Note` varchar(100) NOT NULL,
  `Status` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `Category`
--

INSERT INTO `Category` (`Id`, `Name`, `Note`, `Status`) VALUES
(1, 'Bút ', '34', 1),
(2, '123', 'gerg', 1),
(3, '3', 'gerg', 1),
(4, '1', '1', 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `File1`
--

CREATE TABLE `File1` (
  `Id` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Product_Id` int(11) NOT NULL,
  `Account_Id` int(11) NOT NULL,
  `Manager_Id` int(11) NOT NULL,
  `Start_date` datetime NOT NULL,
  `Note` varchar(100) NOT NULL,
  `Quantity` tinyint(4) NOT NULL,
  `Status` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `File1`
--

INSERT INTO `File1` (`Id`, `Name`, `Product_Id`, `Account_Id`, `Manager_Id`, `Start_date`, `Note`, `Quantity`, `Status`) VALUES
(1, '', 1, 3, 0, '2019-12-31 19:46:31', 'hehe', 3, 1),
(2, '', 1, 3, 0, '2020-01-01 17:31:04', 'khong', 4, 0),
(3, '', 12, 3, 0, '2020-01-01 17:31:09', 'khong1212rf', 4, 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `History`
--

CREATE TABLE `History` (
  `Id` int(11) NOT NULL,
  `Quantity` tinyint(4) NOT NULL,
  `Product_Id` int(11) NOT NULL,
  `Producer_Id` int(11) NOT NULL,
  `Supplier_Id` int(11) NOT NULL,
  `Note` varchar(100) NOT NULL,
  `Price` float NOT NULL,
  `Account_Id` int(11) NOT NULL,
  `Start_date` datetime NOT NULL,
  `Status` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `History`
--

INSERT INTO `History` (`Id`, `Quantity`, `Product_Id`, `Producer_Id`, `Supplier_Id`, `Note`, `Price`, `Account_Id`, `Start_date`, `Status`) VALUES
(1, 13, 1, 1, 1, '', 2000, 1, '2019-12-24 00:00:00', 1),
(2, 13, 10, 7, 4, 'dg', 2000, 1, '2019-12-24 00:00:00', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Producer`
--

CREATE TABLE `Producer` (
  `Id` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Nation` varchar(100) NOT NULL,
  `Status` tinyint(4) NOT NULL,
  `Note` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `Producer`
--

INSERT INTO `Producer` (`Id`, `Name`, `Nation`, `Status`, `Note`) VALUES
(1, 'Thiên Long', 'Việt Nam', 1, 'Ok'),
(3, 'Thiên Long1111', ' ', 1, 'Ok'),
(4, 'Thiên Long1111', ' ', 1, 'Ok'),
(6, 'Thiên Long1111', ' ', 1, 'Ok'),
(7, 'Thiên Long1111', ' ', 1, 'Ok'),
(8, 'Thiên Long1111', ' ', 1, 'Ok'),
(9, 'Thiên Long1111', ' ', 1, 'Ok'),
(10, 'Thiên Long1111', ' ', 1, 'Ok'),
(11, 'Thiên Long1111', ' ', 1, 'Ok'),
(12, 'Thiên Long1111', ' ', 1, 'Ok'),
(13, 'Thiên Long1111', ' ', 1, 'Ok'),
(14, 'Thiên Long1111', ' ', 1, 'Ok'),
(15, 'Thiên Long1111', ' ', 1, 'Ok'),
(19, 'asoidj', ' ', 0, 'asd'),
(21, 'asoidj', ' ', 0, 'asd'),
(22, 'asoidj', ' ', 0, 'asd'),
(23, 'asoidj', ' ', 0, 'asd'),
(24, 'asoidj', ' ', 0, 'asd'),
(25, 'asoidj', ' ', 0, 'asd'),
(26, 'asoidj', ' ', 0, 'asd'),
(27, 'asoidj', ' ', 0, 'asd'),
(28, 'asoidj', ' ', 0, 'asd'),
(29, 'asoidj', ' ', 0, 'asd'),
(30, 'asoidj', ' ', 0, 'asd'),
(31, 'asoidj', ' ', 0, 'asd'),
(32, 'asoidj', ' ', 0, 'asd'),
(33, 'asoidj', ' ', 0, 'asd');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Product`
--

CREATE TABLE `Product` (
  `Id` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `Unit` varchar(10) NOT NULL,
  `Category_Id` int(11) NOT NULL,
  `Img` varchar(100) NOT NULL,
  `Note` varchar(100) NOT NULL,
  `Status` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `Product`
--

INSERT INTO `Product` (`Id`, `Name`, `Quantity`, `Unit`, `Category_Id`, `Img`, `Note`, `Status`) VALUES
(1, 'Bút Thiên Long', 0, 'Cái', 1, 'test', '', 1),
(2, '', 0, 'cai', 1, 'test', '', 1),
(3, '', 0, 'cai', 1, 'test', '', 1),
(4, '', 0, 'cai', 1, 'test', '', 1),
(5, '', 0, 'cai', 2, 'test', '', 1),
(6, '123', 0, 'cai', 1, 'test', 'deo', 1),
(7, 'QUan', 0, 'cai', 1, 'image', 'khong', 1),
(8, 'khanh', 0, 'cai', 1, 'image/Screenshot from 2019-09-14 17-10-42.png', 'qwe', 1),
(9, 'asd', 0, 'cai', 2, 'test', 'ascas', 1),
(10, 'asd', 0, 'cai', 2, 'test', 'ascas', 1),
(11, 'asd', 0, 'cai', 2, 'image/Screenshot from 2019-12-23 13-49-38.png', 'ascas', 1),
(12, 'asd', 0, 'cai', 2, 'test', 'ascas', 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Supplier`
--

CREATE TABLE `Supplier` (
  `Id` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Phone` varchar(100) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Note` varchar(100) NOT NULL,
  `Status` tinyint(4) NOT NULL,
  `Img` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `Supplier`
--

INSERT INTO `Supplier` (`Id`, `Name`, `Phone`, `Address`, `Email`, `Note`, `Status`, `Img`) VALUES
(1, 'Thiên Long', '6192313652', '940  Holden Street', 'soasatsacramentum@gmail.com', '', 1, 'test'),
(2, 'Thiên Long', '61923136521', '20  Holden Street1', '24acramentum@gmail.com', '123123', 1, 'test'),
(3, 'Hà ?ông1', '61923136521', '20  Holden Street1', '24acramentum@gmail.com', '123', 0, 'test'),
(4, 'HàDDông1', '61923136521', '20  Holden Street1', '24acramentum@gmail.com', '123', 0, 'test'),
(5, 'wqe', 'qwe', 'qw', 'qwe', 'qwe', 0, 'test'),
(6, 'eeee', 'ee', 'ee', 'e', 'ee', 0, 'test'),
(7, '1112r', '12r12r', '2r', '21r', '21r', 0, 'test');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `Account`
--
ALTER TABLE `Account`
  ADD PRIMARY KEY (`Id`);

--
-- Chỉ mục cho bảng `Category`
--
ALTER TABLE `Category`
  ADD PRIMARY KEY (`Id`);

--
-- Chỉ mục cho bảng `File1`
--
ALTER TABLE `File1`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `fk_1` (`Product_Id`),
  ADD KEY `fk_2` (`Account_Id`);

--
-- Chỉ mục cho bảng `History`
--
ALTER TABLE `History`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `fk_4` (`Product_Id`),
  ADD KEY `fk_5` (`Producer_Id`),
  ADD KEY `fk_6` (`Supplier_Id`),
  ADD KEY `fk_8` (`Account_Id`);

--
-- Chỉ mục cho bảng `Producer`
--
ALTER TABLE `Producer`
  ADD PRIMARY KEY (`Id`);

--
-- Chỉ mục cho bảng `Product`
--
ALTER TABLE `Product`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `fk_3` (`Category_Id`);

--
-- Chỉ mục cho bảng `Supplier`
--
ALTER TABLE `Supplier`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `Account`
--
ALTER TABLE `Account`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT cho bảng `Category`
--
ALTER TABLE `Category`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT cho bảng `File1`
--
ALTER TABLE `File1`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT cho bảng `History`
--
ALTER TABLE `History`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT cho bảng `Producer`
--
ALTER TABLE `Producer`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;
--
-- AUTO_INCREMENT cho bảng `Product`
--
ALTER TABLE `Product`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT cho bảng `Supplier`
--
ALTER TABLE `Supplier`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `File1`
--
ALTER TABLE `File1`
  ADD CONSTRAINT `fk_1` FOREIGN KEY (`Product_Id`) REFERENCES `Product` (`Id`),
  ADD CONSTRAINT `fk_2` FOREIGN KEY (`Account_Id`) REFERENCES `Account` (`Id`);

--
-- Các ràng buộc cho bảng `History`
--
ALTER TABLE `History`
  ADD CONSTRAINT `fk_4` FOREIGN KEY (`Product_Id`) REFERENCES `Product` (`Id`),
  ADD CONSTRAINT `fk_5` FOREIGN KEY (`Producer_Id`) REFERENCES `Producer` (`Id`),
  ADD CONSTRAINT `fk_6` FOREIGN KEY (`Supplier_Id`) REFERENCES `Supplier` (`Id`),
  ADD CONSTRAINT `fk_8` FOREIGN KEY (`Account_Id`) REFERENCES `Account` (`Id`);

--
-- Các ràng buộc cho bảng `Product`
--
ALTER TABLE `Product`
  ADD CONSTRAINT `fk_3` FOREIGN KEY (`Category_Id`) REFERENCES `Category` (`Id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
