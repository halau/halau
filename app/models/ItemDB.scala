package models

import play.api._
import play.api.db._
import play.api.Play.current
import org.scalaquery.session._
import org.scalaquery.ql._
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql.basic.{ BasicTable => Table }
import org.scalaquery.ql.basic.BasicDriver.Implicit._
import org.scalaquery.ql.basic.BasicProfile
import org.scalaquery.ql.basic.BasicInsertInvoker
import org.scalaquery.ql.extended.PostgresDriver.Implicit._
import org.scalaquery.ql.extended.H2Driver.Implicit._

import java.net.URI
import java.util.{ Date => UtilDate }
import java.sql.{ Timestamp => SQLTimestamp }

object ItemDB extends Table[Item]("ITEM") {

  def id = column[Int]("ID", O.PrimaryKey)
  def money = column[Int]("MONEY", O.NotNull)
  def kind = column[Option[String]]("KIND")
  def location = column[Option[String]]("LOCATION")

  def * = id~money~kind~location <> (Item, Item.unapply _)

  def db = Database.forDataSource(DB.getDataSource())
  
  def add(item : Item) : Unit = db.withSession { implicit db : Session =>
    (money~kind~location).insert(item.money, item.kind, item.location)
  }

  def update(item : Item) : Unit =
    db.withSession { implicit db : Session =>
      ItemDB.where( x => x.id is id).update(item)
    }

  def remove(id : Int) : Unit =
    db.withSession { implicit db : Session => 
      ItemDB.where( x => x.id is id).delete
    }

  
  def findAll : List[Item] = db.withSession { implicit db : Session =>
    (for (t <- this; _ <- Query) yield t.*).list
  }
  
/*
  def findAll(limit:Int=0) : List[Activity] = db.withSession { implicit db : Session =>
    val l = if (limit == 0) Play.configuration.getInt("activity.fetch_limit").getOrElse(20)
            else limit
    (for (t <- this; _ <- Query orderBy Ordering.Desc(t.createdAt)) yield t.*).take(l).list
  }
*/
  def find(id : Int) : Option[Item] =
    db.withSession { implicit db : Session =>
      ItemDB.where( x => x.id is id).map(_.*).firstOption
    }

/*  def tee[A]( x : A)(action : A => Unit) : A = {
    action(x)
    x
  }

  def addAll(as : Seq[Activity]) : Option[Int] = db.withSession { implicit db : Session =>
    val ys = ActivityDB.where( x => x.id inSet as.map(_.id)).map(_.id).list
    val zs = as filterNot ( (a : Activity) => ys.contains(a.id) )
    tee(ActivityDB.insertAll(zs:_*)) { _ =>
      zs.foreach( z => notify(z))
    }
  }*/
}