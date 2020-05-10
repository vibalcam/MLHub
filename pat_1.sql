CREATE DATABASE  IF NOT EXISTS `pat_1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pat_1`;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: pat_1
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `access_level`
--

DROP TABLE IF EXISTS `access_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `access_level` (
  `id` int NOT NULL,
  `subirResultados` tinyint NOT NULL,
  `accesoResultados` tinyint NOT NULL,
  `subirCodigo` tinyint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `access_level`
--

LOCK TABLES `access_level` WRITE;
/*!40000 ALTER TABLE `access_level` DISABLE KEYS */;
INSERT INTO `access_level` VALUES (0,1,1,1),(50,1,1,1),(70,1,0,1),(99,0,0,1);
/*!40000 ALTER TABLE `access_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compras`
--

DROP TABLE IF EXISTS `compras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compras` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `producto` int NOT NULL,
  `cliente` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `compras_producto_idx` (`producto`),
  KEY `compras_cliente_idx` (`cliente`),
  CONSTRAINT `compras_cliente` FOREIGN KEY (`cliente`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `compras_producto` FOREIGN KEY (`producto`) REFERENCES `productos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compras`
--

LOCK TABLES `compras` WRITE;
/*!40000 ALTER TABLE `compras` DISABLE KEYS */;
INSERT INTO `compras` VALUES (1,'2020-05-02',2,1),(2,'2020-04-02',1,1),(3,'2020-05-02',2,1),(4,'2020-05-03',2,1),(5,'2020-05-03',1,1),(8,'2020-05-10',3,9),(9,'2020-05-10',2,9),(10,'2020-05-10',1,9),(11,'2020-05-10',2,9),(13,'2020-05-10',2,20);
/*!40000 ALTER TABLE `compras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entries`
--

DROP TABLE IF EXISTS `entries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entries` (
  `id` int NOT NULL AUTO_INCREMENT,
  `creator` int NOT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `entries_creator_idx` (`creator`),
  CONSTRAINT `entries_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entries`
--

LOCK TABLES `entries` WRITE;
/*!40000 ALTER TABLE `entries` DISABLE KEYS */;
/*!40000 ALTER TABLE `entries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metodos`
--

DROP TABLE IF EXISTS `metodos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `metodos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `eficacia` int DEFAULT NULL,
  `tiempo` int DEFAULT NULL,
  `proyecto` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `metodos_proyecto_idx` (`proyecto`),
  CONSTRAINT `metodos_proyecto` FOREIGN KEY (`proyecto`) REFERENCES `proyectos` (`nombre`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metodos`
--

LOCK TABLES `metodos` WRITE;
/*!40000 ALTER TABLE `metodos` DISABLE KEYS */;
INSERT INTO `metodos` VALUES (1,'networks',80,5,'titanic'),(2,'networks',80,5,'bank'),(3,'clustering',50,3,'titanic'),(4,'networks',80,5,'cars'),(5,'regression',1,1,'titanic'),(6,'clustering',40,3,'bank'),(7,'regression',40,1,'bank'),(8,'trees',60,4,'titanic');
/*!40000 ALTER TABLE `metodos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `precio` double unsigned NOT NULL,
  `porcentajeOferta` tinyint unsigned NOT NULL DEFAULT '0',
  `accessLevel` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  KEY `productos_access_level_idx` (`accessLevel`),
  CONSTRAINT `productos_access_level` FOREIGN KEY (`accessLevel`) REFERENCES `access_level` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (0,'admin',0,0,0),(1,'Gratuito',0,0,99),(2,'Pro',10,0,70),(3,'Prime',20,75,50);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyectos`
--

DROP TABLE IF EXISTS `proyectos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proyectos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `usuario` int DEFAULT NULL,
  `codigo` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyectos`
--

LOCK TABLES `proyectos` WRITE;
/*!40000 ALTER TABLE `proyectos` DISABLE KEYS */;
INSERT INTO `proyectos` VALUES (1,'titanic',9,'<div id=\"content\">\r\n        <nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\r\n            <div class=\"container-fluid\">\r\n                <button type=\"button\" id=\"sidebarCollapse\" class=\"btn btn-secondary\">\r\n                    <i class=\"fas fa-align-left\"></i>\r\n                    <span>Toggle Sidebar</span>\r\n                </button>\r\n\r\n                <form class=\"row form-inline ml-4 d-none d-lg-inline\" id=\"formBuscar\" name=\"formBuscar\" method=\"get\" action=\"${pageContext.request.contextPath}/inicio\">\r\n                    <input class=\"form-control mr-sm-2 col-7\" type=\"search\" name=\"searchName\" required placeholder=\"Buscar\" aria-label=\"Buscar\">\r\n                    <button class=\"btn btn-success\" type=\"submit\" name=\"action\" value=\"searchEntry\">Buscar</button>\r\n                </form>\r\n\r\n                <button class=\"btn btn-dark d-inline-block d-lg-none ml-auto\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarSupportedContent\" aria-controls=\"navbarSupportedContent\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\r\n                    <i class=\"fas fa-align-justify\"></i>\r\n                    Mostrar navegaciÃ³n\r\n                </button>\r\n\r\n                <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\r\n                    <ul class=\"nav navbar-nav ml-auto\">\r\n                        <li class=\"nav-item\">\r\n                            <a class=\"nav-link\" href=\"${pageContext.request.contextPath}/inicio\">Inicio</a>\r\n                        </li>\r\n                        <li class=\"nav-item\">\r\n                            <a class=\"nav-link\" href=\"${pageContext.request.contextPath}/inicio/subscripcion\">SubscripciÃ³n</a>\r\n                        </li>\r\n                        <li class=\"nav-item active\">\r\n                            <a class=\"nav-link\" href=\"${pageContext.request.contextPath}/inicio/historial\">Historial</a>\r\n                        </li>\r\n                        <li class=\"nav-item\">\r\n                            <a class=\"nav-link\" href=\"${pageContext.request.contextPath}/inicio/modificar\">Usuario</a>\r\n                        </li>\r\n                        <li class=\"nav-item\">\r\n                            <form id=\"formCerrarSesion\" name=\"formCerrarSesion\" method=\"post\" action=\"${pageContext.request.contextPath}/inicio\">\r\n                                <a href=\"javascript:$(\'#formCerrarSesion\').submit();\" id=\"linkCerrarSesion\" class=\"nav-link\">Cerrar sesiÃ³n</a>\r\n                                <input type=\"hidden\" name=\"action\" value=\"cerrarSesion\">\r\n                            </form>\r\n                        </li>\r\n                    </ul>\r\n                </div>\r\n            </div>\r\n        </nav>'),(2,'bank',9,'package servlets;\r\n\r\nimport dao.MLDao;\r\nimport dominio.AccessLevel;\r\nimport dominio.Metodo;\r\nimport dominio.Proyecto;\r\nimport dominio.Usuario;\r\n\r\nimport javax.servlet.ServletException;\r\nimport javax.servlet.annotation.WebServlet;\r\nimport javax.servlet.http.*;\r\nimport java.io.IOException;\r\nimport java.sql.SQLException;\r\n\r\n@WebServlet(name = \"ProyectoServlet\", urlPatterns = \"/inicio/proyecto\")\r\npublic class ProyectoServlet extends HttpServlet {\r\n    @Override\r\n    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n        response.sendRedirect(getServletContext().getContextPath());\r\n    }\r\n\r\n    @Override\r\n    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n        String proyecto = request.getParameter(\"peticion\");\r\n        String proyecto2 = request.getParameter(\"peticion2\");\r\n\r\n        if((proyecto == null) || (proyecto.isBlank())){\r\n            if((proyecto2 == null) || (proyecto2.isBlank())) {\r\n                response.sendRedirect(getServletContext().getContextPath() + \"/inicio\");\r\n                return;\r\n            }\r\n            proyecto = proyecto2;\r\n        }\r\n\r\n        String nombre = request.getParameter(\"nombre\");\r\n        String eficacia = request.getParameter(\"eficacia\");\r\n        String tiempo = request.getParameter(\"tiempo\");\r\n        String codigo = request.getParameter(\"codigo\");\r\n\r\n        MLDao dao = null;\r\n\r\n        try{\r\n            dao = MLDao.getInstance();\r\n\r\n            if(!((nombre == null) || (eficacia == null) || (tiempo == null)) && !(nombre.isBlank() || eficacia.isBlank() || tiempo.isBlank())){\r\n                dao.addMetodo(new Metodo(nombre, Integer.parseInt(eficacia), Integer.parseInt(tiempo)), proyecto);\r\n            }\r\n\r\n            if((codigo != null) && (proyecto2 != null)){\r\n                dao.updateCodigo(proyecto, codigo);\r\n            }\r\n\r\n//            if(dao.getUsuario(proyecto) == ((Usuario) request.getSession().getAttribute(\"userLogged\")).getId()){\r\n//                request.setAttribute(\"addPosible\",1);\r\n//            } else{\r\n//                request.setAttribute(\"addPosible\",0);\r\n//            }\r\n\r\n            int id = ((Usuario) request.getSession().getAttribute(\"userLogged\")).getId();\r\n\r\n            request.setAttribute(\"proyecto\", dao.getProyecto(proyecto));\r\n//            request.setAttribute(\"nombreProyecto\", proyecto);\r\n//            request.setAttribute(\"codigo\",dao.getCodigo(proyecto).replaceAll(\"&n\",\"\\n\"));\r\n//            request.setAttribute(\"level\", dao.getSubscripcion(id));\r\n            request.getRequestDispatcher(\"/inicio/proyecto/view\").forward(request,response);\r\n        } catch (SQLException | ClassNotFoundException e) {\r\n            e.printStackTrace();\r\n            response.sendRedirect(getServletContext().getContextPath() + \"/inicio\");\r\n        } finally {\r\n            if(dao != null) {\r\n                try {\r\n                    dao.close();\r\n                } catch (SQLException e) {\r\n                    e.printStackTrace();\r\n                }\r\n            }\r\n        }\r\n    }\r\n\r\n}\r\n'),(3,'cars',1,'d'),(10,'libros',1,'');
/*!40000 ALTER TABLE `proyectos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `apellidos` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `usuario` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
  `password` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `subscripcion` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario_UNIQUE` (`usuario`),
  KEY `users_subscripcion_idx` (`subscripcion`),
  CONSTRAINT `users_subscripcion` FOREIGN KEY (`subscripcion`) REFERENCES `productos` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (0,'admin',NULL,'admin','admin',0),(1,'pat',NULL,'pat','pat',1),(9,'oscar','gonzalez','oscar1','pepe',3),(13,'pablo','llorente','pablo1','pepe1',1),(15,'pepe','manolas','fuego30','alocado',2),(16,'','','olp33','prrvr',1),(20,'ruben','llorente','user1','lolo',2),(22,'','','mon89','lolo',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-10 20:26:50
