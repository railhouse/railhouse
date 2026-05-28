/*
 * Author: Jamius Siam
 * Since: 28/05/2026
 */

import { Link } from "@tanstack/react-router";
import { Sparkles, Inbox } from "lucide-react";

const Header = () => {
  return (
    <div className="flex h-5.5 px-3">
      <div>
        <Link to="/">
          <img className="h-[15.22px]" src="/flightdrift_logo_light.svg" alt="logo" />
        </Link>
      </div>
      <div className="ms-auto flex gap-3.5 justify-center items-center">
        <Sparkles strokeWidth={1.8} size={19} color="#0090FF" />
        <Inbox strokeWidth={1.5} size={20} color="#8E8E8E" />
        <img src="/avatar.jpg" className="w-[22px] h-[22px] rounded-full" />
      </div>
    </div>
  );
};

export default Header;
