//
//  TbtNavigation.swift
//  TbtNavigation
//
//  Created by Hamza El Yousfi on 11/13/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

import Foundation
import MapboxDirections
import MapboxCoreNavigation
import MapboxNavigation

@objc(TbtNavigation)
class TbtNavigation: NSObject {
  @objc
  func startNavigation(_ options: AnyObject) -> Void{
    let source = options["source"] as AnyObject;
    let lat1 = source["lat"] as! Double;
    let lon1 = source["lon"] as! Double;
    let sourceName = source["name"] as! String;
  
    let dest = options["dest"] as AnyObject;
    let lat2 = dest["lat"] as! Double;
    let lon2 = dest["lon"] as! Double;
    let destName = dest["name"] as! String;
    
    let origin = Waypoint(coordinate: CLLocationCoordinate2D(latitude: CLLocationDegrees(lat1), longitude:CLLocationDegrees(lon1)), name: sourceName)
    let destination = Waypoint(coordinate: CLLocationCoordinate2D(latitude: CLLocationDegrees(lat2), longitude:CLLocationDegrees(lon2)), name: destName)
    let options = NavigationRouteOptions(waypoints: [origin, destination])
    Directions.shared.calculate(options) { (waypoints, routes, error) in
      guard let route = routes?.first else { return }
      let viewController = NavigationViewController(for: route)
      let appDelegate = UIApplication.shared.delegate
      appDelegate!.window!!.rootViewController!.present(viewController, animated: true, completion: nil)
    }
  }
  
  @objc static func requiresMainQueueSetup() -> Bool {
    return false
  }
}
